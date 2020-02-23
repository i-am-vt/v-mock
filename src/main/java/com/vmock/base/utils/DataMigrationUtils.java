package com.vmock.base.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 数据迁移工具
 * <p>
 * 对应：一旦版本更新后，嵌入式数据库不方便数据迁移的问题
 *
 * @author vt
 * @since 2020.2.20
 */
@Slf4j
public class DataMigrationUtils {

    /**
     * 时间格式化 Formatter- yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    /**
     * 打印信息模版
     */
    private static final String PRINT_ATTR_TEMPLATE = "编号：%s. 名称: %s 首次启动时间: %s  最近操作时间: %s";


    /**
     * db文件位置
     */
    private static final String DB_FILE_PATH = "db/v-mock.sqlite";


    /**
     * 数据迁移操作
     */
    @SneakyThrows
    public static void dataMigrationCheck() {
        // 检查是否需要数据迁移
        String isNeedDataMigration = System.getProperty("dm");
        // -Ddm参数传了【true, yes, y, t, ok, 1, on, 是, 对, 真】 都行
        if (!BooleanUtil.toBoolean(isNeedDataMigration)) {
            log.info("Without data migration. ");
            return;
        }
        // 获取历史版本的数据文件
        List<File> historyDataFileList = getHistoryDataFileList();
        // check empty
        if (CollUtil.isEmpty(historyDataFileList)) {
            log.info("No history database file found. Run application..");
            return;
        }
        // 选出旧版本的数据库文件
        File targetOldDbFile = getTargetFile(historyDataFileList);
        log.info("Find old version db file {}. start migration...", targetOldDbFile.getName());
        // 获取当前ClassLoader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // DB文件URL
        URL currentDbFileUrl = classLoader.getResource(DB_FILE_PATH);
        // 以 link org.sqlite.SQLiteConnection 源码中的命名方式迁移DB文件
        String dbFileName = String.format("sqlite-jdbc-tmp-%d.db", currentDbFileUrl.hashCode());
        String tempFolder = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();
        File currentDbFile = new File(tempFolder, dbFileName);
        // 将旧数据库文件复制一份，并用当前数据库命名方式命名以完成数据迁移
        File currentDb = FileUtil.copy(targetOldDbFile, currentDbFile, true);
        log.info("Data file {} migration success", currentDb.getName());
    }


    /**
     * 获取所有历史DB文件集合
     *
     * @return List<File>集合
     */
    private static List<File> getHistoryDataFileList() {
        // 历史数据库文件都是存在临时目录下
        String tempFilePath = System.getProperty("java.io.tmpdir");
        File[] tempFileArr = FileUtil.ls(tempFilePath);
        // 临时目录没文件，返回 null
        if (ArrayUtil.isEmpty(tempFileArr)) {
            return null;
        }
        // 过滤掉非db文件
        Stream<File> filesStream = Arrays.stream(tempFileArr)
                .filter(file -> file.isFile() && file.getName().matches("sqlite-jdbc-tmp-.*\\.db"));
        // 转为List返回
        List<File> fileList = filesStream.collect(Collectors.toList());
        return fileList;
    }

    /**
     * 打印文件附加信息
     *
     * @param index 编号
     * @param file  文件对象
     */
    @SneakyThrows
    private static void printFileAttr(int index, File file) {
        // 读取附加信息
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        // 获取创建时间 更新时间
        Instant createTimeInstant = basicFileAttributes.creationTime().toInstant();
        Instant updateTimeInstant = basicFileAttributes.lastModifiedTime().toInstant();
        // 以上遍历加入模版中，打印
        Console.log(String.format(PRINT_ATTR_TEMPLATE,
                index,
                file.getName(),
                TIME_FORMATTER.format(createTimeInstant),
                TIME_FORMATTER.format(updateTimeInstant)
        ));
    }

    /**
     * 返回指定的旧版本历史数据库
     *
     * @param historyDataFileList 从临时文件筛选后的列表
     * @return File 指定的文件
     */
    private static File getTargetFile(List<File> historyDataFileList) {
        // 如果只有一个，那么就返回这个
        File historyDataFile = historyDataFileList.get(0);
        // 如果有多个，则让用户推断是哪一个
        if (historyDataFileList.size() > 1) {
            // tips to user:
            Console.log("-----------------------------------------------"
                    + "\n发现多个数据库文件，请根据[旧版程序]'首次启动时间'，或[旧版程序]'最近操作时间'确定需要迁移的数据库文件."
                    + "\n注意，这将会覆盖新版本中的数据，一般迁移将在首次启动新版本时进行，并且第二次启动去掉-Ddm参数即可\n");
            // Create map: <编号, DB文件>
            int capacity = (int) (historyDataFileList.size() / 0.75F) + 1;
            Map<String, File> fileMap = new HashMap<>(capacity);
            // 遍历打印每个DB文件的信息 以供用户推断
            for (int i = 0; i < historyDataFileList.size(); i++) {
                File tempDbFile = historyDataFileList.get(i);
                // 打印这些属性
                printFileAttr(i, tempDbFile);
                // 以"<编号, DB文件>"的映射存入Map
                fileMap.put(String.valueOf(i), tempDbFile);
            }
            Console.log("\n输入编号，以选择文件完成数据迁移.");
            // 校验输入存在编号
            while ((historyDataFile = fileMap.get(Console.input())) == null) {
                Console.log("请输入正确的编号.");
            }
        }
        return historyDataFile;
    }
}
