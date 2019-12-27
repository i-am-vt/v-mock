// table封装
var table = {
    config: {},
    options: {},
    set: function(id) {
        if ($.common.getLength(table.config) > 1) {
            var tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find(".table").attr("id") : id;
            if ($.common.isNotEmpty(tableId)) {
                table.options = table.get(tableId)
            }
        }
    },
    get: function(id) {
        return table.config[id]
    },
    rememberSelecteds: {},
    rememberSelectedIds: {}
};
(function($) {
    $.extend({
        _tree: {},
        bttTable: {},
        table: {
            init: function(options) {
                var defaults = {
                    id: "bootstrap-table",
                    type: 0,
                    height: undefined,
                    sidePagination: "server",
                    sortName: "",
                    sortOrder: "asc",
                    pagination: true,
                    pageSize: 10,
                    pageList: [10, 25, 50],
                    toolbar: "toolbar",
                    striped: false,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    clickToSelect: false,
                    mobileResponsive: true,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    rightFixedColumns: false,
                    rightFixedNumber: 0,
                    queryParams: $.table.queryParams,
                    rowStyle: {},
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $('#' + options.id).bootstrapTable({
                    id: options.id,
                    url: options.url,
                    contentType: "application/x-www-form-urlencoded",
                    method: 'post',
                    cache: false,
                    height: options.height,
                    striped: options.striped,
                    sortable: true,
                    sortStable: true,
                    sortName: options.sortName,
                    sortOrder: options.sortOrder,
                    pagination: options.pagination,
                    pageNumber: 1,
                    pageSize: options.pageSize,
                    pageList: options.pageList,
                    firstLoad: options.firstLoad,
                    escape: options.escape,
                    showFooter: options.showFooter,
                    iconSize: 'outline',
                    toolbar: '#' + options.toolbar,
                    sidePagination: options.sidePagination,
                    search: options.search,
                    searchText: options.searchText,
                    showSearch: options.showSearch,
                    showPageGo: options.showPageGo,
                    showRefresh: options.showRefresh,
                    showColumns: options.showColumns,
                    showToggle: options.showToggle,
                    showExport: options.showExport,
                    uniqueId: options.uniqueId,
                    clickToSelect: options.clickToSelect,
                    mobileResponsive: options.mobileResponsive,
                    detailView: options.detailView,
                    onClickRow: options.onClickRow,
                    onDblClickRow: options.onDblClickRow,
                    onClickCell: options.onClickCell,
                    onDblClickCell: options.onDblClickCell,
                    onEditableSave: options.onEditableSave,
                    onExpandRow: options.onExpandRow,
                    rememberSelected: options.rememberSelected,
                    fixedColumns: options.fixedColumns,
                    fixedNumber: options.fixedNumber,
                    rightFixedColumns: options.rightFixedColumns,
                    rightFixedNumber: options.rightFixedNumber,
                    onReorderRow: options.onReorderRow,
                    queryParams: options.queryParams,
                    rowStyle: options.rowStyle,
                    columns: options.columns,
                    responseHandler: $.table.responseHandler,
                    onLoadSuccess: $.table.onLoadSuccess,
                    exportOptions: options.exportOptions,
                    detailFormatter: options.detailFormatter,
                })
            },
            getOptionsIds: function(separator) {
                var _separator = $.common.isEmpty(separator) ? "," : separator;
                var optionsIds = "";
                $.each(table.config, function(key, value) {
                    optionsIds += "#" + key + _separator
                });
                return optionsIds.substring(0, optionsIds.length - 1)
            },
            queryParams: function(params) {
                var curParams = {
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    searchValue: params.search,
                    orderByColumn: params.sort,
                    isAsc: params.order
                };
                var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
                return $.extend(curParams, $.common.formToJSON(currentId))
            },
            responseHandler: function(res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res)
                }
                if (res.code == 0) {
                    if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
                        return res.rows
                    } else {
                        if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                            var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                            $.each(res.rows, function(i, row) {
                                row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1
                            })
                        }
                        return {
                            rows: res.rows,
                            total: res.total
                        }
                    }
                } else {
                    $.modal.alertWarning(res.msg);
                    return {
                        rows: [],
                        total: 0
                    }
                }
            },
            initEvent: function() {
                var optionsIds = $.table.getOptionsIds();
                $(optionsIds).on(TABLE_EVENTS, function() {
                    table.set($(this).attr("id"))
                });
                $(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function(e, rows) {
                    var rowIds = $.table.affectedRowIds(rows);
                    if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                        func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
                        var selectedIds = table.rememberSelectedIds[table.options.id];
                        if ($.common.isNotEmpty(selectedIds)) {
                            table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds)
                        } else {
                            table.rememberSelectedIds[table.options.id] = _[func]([], rowIds)
                        }
                        var selectedRows = table.rememberSelecteds[table.options.id];
                        if ($.common.isNotEmpty(selectedRows)) {
                            table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows)
                        } else {
                            table.rememberSelecteds[table.options.id] = _[func]([], rows)
                        }
                    }
                });
                $(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function() {
                    var toolbar = table.options.toolbar;
                    var uniqueId = table.options.uniqueId;
                    var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
                    $('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
                    $('#' + toolbar + ' .single').toggleClass('disabled', rows.length != 1)
                });
                $(optionsIds).off("click").on("click", '.img-circle', function() {
                    var src = $(this).attr('src');
                    var target = $(this).data('target');
                    var height = $(this).data('height');
                    var width = $(this).data('width');
                    if ($.common.equals("self", target)) {
                        layer.open({
                            title: false,
                            type: 1,
                            closeBtn: true,
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
                        })
                    } else if ($.common.equals("blank", target)) {
                        window.open(src)
                    }
                });
                $(optionsIds).on("click", '.tooltip-show', function() {
                    var target = $(this).data('target');
                    var input = $(this).prev();
                    if ($.common.equals("copy", target)) {
                        input.select();
                        document.execCommand("copy")
                    } else if ($.common.equals("open", target)) {
                        parent.layer.alert(input.val(), {
                            title: "信息内容",
                            shadeClose: true,
                            btn: ['确认'],
                            btnclass: ['btn btn-primary'],
                        })
                    }
                })
            },
            onLoadSuccess: function(data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data)
                }
                $("[data-toggle='tooltip']").tooltip()
            },
            destroy: function(tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('destroy')
            },
            serialNumber: function(index, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                var tableParams = $("#" + currentId).bootstrapTable('getOptions');
                var pageSize = tableParams.pageSize;
                var pageNumber = tableParams.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1
            },
            tooltip: function(value, length, target) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                var _target = $.common.isEmpty(target) ? 'copy' : target;
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g, "’");
                    var actions = [];
                    actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf("<a href='###' class='tooltip-show' data-toggle='tooltip' data-target='%s' title='%s'>%s</a>", _target, _value, _text));
                    return actions.join('')
                } else {
                    _text = _value;
                    return _text
                }
            },
            dropdownToggle: function(value) {
                var actions = [];
                actions.push('<div class="btn-group">');
                actions.push('<button type="button" class="btn btn-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false">');
                actions.push('<i class="fa fa-cog"></i>&nbsp;<span class="fa fa-chevron-down"></span></button>');
                actions.push('<ul class="dropdown-menu">');
                actions.push(value.replace(/<a/g, "<li><a").replace(/<\/a>/g, "</a></li>"));
                actions.push('</ul>');
                actions.push('</div>');
                return actions.join('')
            },
            imageView: function(value, height, width, target) {
                if ($.common.isEmpty(width)) {
                    width = 'auto'
                }
                if ($.common.isEmpty(height)) {
                    height = 'auto'
                }
                var _target = $.common.isEmpty(target) ? 'self' : target;
                if ($.common.isNotEmpty(value)) {
                    return $.common.sprintf("<img class='img-circle img-xs' data-height='%s' data-width='%s' data-target='%s' src='%s'/>", height, width, _target, value)
                } else {
                    return $.common.nullToStr(value)
                }
            },
            search: function(formId, tableId, data) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
                params.queryParams = function(params) {
                    var search = $.common.formToJSON(currentId);
                    if ($.common.isNotEmpty(data)) {
                        $.each(data, function(key) {
                            search[key] = data[key]
                        })
                    }
                    if (search["params[beginTime]"]) {
                        search["params[beginTime]"] = new Date(search["params[beginTime]"]).getTime()
                    }
                    if (search["params[endTime]"]) {
                        search["params[endTime]"] = new Date(search["params[endTime]"]).getTime()
                    }
                    search.pageSize = params.limit;
                    search.pageNum = params.offset / params.limit + 1;
                    search.searchValue = params.search;
                    search.orderByColumn = params.sort;
                    search.isAsc = params.order;
                    return search
                }
                if ($.common.isNotEmpty(tableId)) {
                    $("#" + tableId).bootstrapTable('refresh', params)
                } else {
                    $("#" + table.options.id).bootstrapTable('refresh', params)
                }
            },
            exportExcel: function(formId) {
                table.set();
                $.modal.confirm("确定导出所有" + table.options.modalName + "吗？", function() {
                    var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                    $.modal.loading("正在导出数据，请稍后...");
                    $.post(table.options.exportUrl, $("#" + currentId).serializeArray(), function(result) {
                        if (result.code == web_status.SUCCESS) {
                            window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg)
                        }
                        $.modal.closeLoading()
                    })
                })
            },
            importTemplate: function() {
                table.set();
                $.get(table.options.importTemplateUrl, function(result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true
                    } else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
                        $.modal.alertError(result.msg)
                    }
                })
            },
            refresh: function(tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                })
            },
            selectColumns: function(column) {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function(row) {
                    return row[column]
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(table.rememberSelecteds[table.options.id], function(row) {
                            return row[column]
                        })
                    }
                }
                return $.common.uniqueFn(rows)
            },
            affectedRowIds: function(rows) {
                var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                var rowIds;
                if ($.isArray(rows)) {
                    rowIds = $.map(rows, function(row) {
                        return row[column]
                    })
                } else {
                    rowIds = [rows[column]]
                }
                return rowIds
            },
            selectFirstColumns: function() {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function(row) {
                    return row[table.options.columns[1].field]
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(selectedRows, function(row) {
                            return row[table.options.columns[1].field]
                        })
                    }
                }
                return $.common.uniqueFn(rows)
            },
            selectDictLabel: function(datas, value) {
                var actions = [];
                $.each(datas, function(index, dict) {
                    if (dict.dictValue == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false
                    }
                });
                return actions.join('')
            },
            showColumn: function(column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('showColumn', column)
            },
            hideColumn: function(column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('hideColumn', column)
            }
        },
        treeTable: {
            init: function(options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1,
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 1,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,
                    parentCode: options.parentCode,
                    type: 'post',
                    url: options.url,
                    data: options.data,
                    ajaxParams: options.ajaxParams,
                    rootIdValue: options.rootIdValue,
                    height: options.height,
                    expandColumn: options.expandColumn,
                    striped: options.striped,
                    bordered: true,
                    toolbar: '#' + options.toolbar,
                    showSearch: options.showSearch,
                    showRefresh: options.showRefresh,
                    showColumns: options.showColumns,
                    expandAll: options.expandAll,
                    expandFirst: options.expandFirst,
                    columns: options.columns,
                    responseHandler: $.treeTable.responseHandler
                })
            },
            search: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params)
            },
            refresh: function() {
                $.bttTable.bootstrapTreeTable('refresh')
            },
            selectColumns: function(column) {
                var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function(row) {
                    return row[column]
                });
                return $.common.uniqueFn(rows)
            },
            responseHandler: function(data) {
                if (data.code != undefined && data.code != 0) {
                    $.modal.alertWarning(data.msg);
                    return []
                } else {
                    return data
                }
            },
        },
        form: {
            reset: function(formId, tableId) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
                if (table.options.type == table_type.bootstrapTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTable('refresh')
                    } else {
                        $("#" + tableId).bootstrapTable('refresh')
                    }
                }
            },
            selectCheckeds: function(name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function(i) {
                    if (0 == i) {
                        checkeds = $(this).val()
                    } else {
                        checkeds += ("," + $(this).val())
                    }
                });
                return checkeds
            },
            selectSelects: function(name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function(i) {
                    if (0 == i) {
                        selects = $(this).val()
                    } else {
                        selects += ("," + $(this).val())
                    }
                });
                return selects
            }
        },
        modal: {
            icon: function(type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0
                } else if (type == modal_status.SUCCESS) {
                    icon = 1
                } else if (type == modal_status.FAIL) {
                    icon = 2
                } else {
                    icon = 3
                }
                return icon
            },
            msg: function(content, type) {
                if (type != undefined) {
                    layer.msg(content, {
                        icon: $.modal.icon(type),
                        time: 1000,
                        shift: 5
                    })
                } else {
                    layer.msg(content)
                }
            },
            msgError: function(content) {
                $.modal.msg(content, modal_status.FAIL)
            },
            msgSuccess: function(content) {
                $.modal.msg(content, modal_status.SUCCESS)
            },
            msgWarning: function(content) {
                $.modal.msg(content, modal_status.WARNING)
            },
            alert: function(content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "系统提示",
                    btn: ['确认'],
                    btnclass: ['btn btn-primary'],
                })
            },
            msgReload: function(msg, type) {
                layer.msg(msg, {
                    icon: $.modal.icon(type),
                    time: 500,
                    shade: [0.1, '#8F8F8F']
                }, function() {
                    $.modal.reload()
                })
            },
            alertError: function(content) {
                $.modal.alert(content, modal_status.FAIL)
            },
            alertSuccess: function(content) {
                $.modal.alert(content, modal_status.SUCCESS)
            },
            alertWarning: function(content) {
                $.modal.alert(content, modal_status.WARNING)
            },
            close: function() {
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index)
            },
            closeAll: function() {
                layer.closeAll()
            },
            confirm: function(content, callBack) {
                layer.confirm(content, {
                    icon: 3,
                    title: "系统提示",
                    btn: ['确认', '取消']
                }, function(index) {
                    layer.close(index);
                    callBack(true)
                })
            },
            open: function(title, url, width, height, callback) {
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto'
                }
                if ($.common.isEmpty(title)) {
                    title = false
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html"
                }
                if ($.common.isEmpty(width)) {
                    width = 800
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50)
                }
                if ($.common.isEmpty(callback)) {
                    callback = function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero)
                    }
                }
                if ((width = width.toString()).indexOf('px') == -1 && width.indexOf('%') == -1) {
                    width += 'px'
                }
                if ((height = height.toString()).indexOf('px') == -1 && height.indexOf('%') == -1) {
                    height += 'px'
                }
                layer.open({
                    type: 2,
                    area: [width, height],
                    fix: false,
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    shadeClose: true,
                    yes: callback,
                    cancel: function(index) {
                        return true
                    }
                })
            },
            openWithoutYes: function(title, url, width, height, callback) {
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto'
                }
                if ($.common.isEmpty(title)) {
                    title = false
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html"
                }
                if ($.common.isEmpty(width)) {
                    width = 800
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50)
                }
                if ($.common.isEmpty(callback)) {
                    callback = function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero)
                    }
                }
                if ((width = width.toString()).indexOf('px') == -1 && width.indexOf('%') == -1) {
                    width += 'px'
                }
                if ((height = height.toString()).indexOf('px') == -1 && height.indexOf('%') == -1) {
                    height += 'px'
                }
                layer.open({
                    type: 2,
                    area: [width, height],
                    fix: false,
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['关闭'],
                    shadeClose: true
                })
            },
            openOptions: function(options) {
                var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
                var _width = $.common.isEmpty(options.width) ? "800" : options.width;
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
                if ($.common.isEmpty(options.yes)) {
                    options.yes = function(index, layero) {
                        options.callBack(index, layero)
                    }
                }
                if ((_width = _width.toString()).indexOf('px') == -1 && _width.indexOf('%') == -1) {
                    _width += 'px'
                }
                if ((_height = _height.toString()).indexOf('px') == -1 && _height.indexOf('%') == -1) {
                    _height += 'px'
                }
                layer.open({
                    type: 2,
                    maxmin: true,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width, _height],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
                    yes: options.yes,
                    cancel: function() {
                        return true
                    }
                })
            },
            openFull: function(title, url, width, height) {
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    width = 'auto';
                    height = 'auto'
                }
                if ($.common.isEmpty(title)) {
                    title = false
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html"
                }
                if ($.common.isEmpty(width)) {
                    width = 800
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50)
                }
                var index = layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    shadeClose: true,
                    yes: function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero)
                    },
                    cancel: function(index) {
                        return true
                    }
                });
                layer.full(index)
            },
            openTab: function(title, url) {
                createMenuItem(url, title)
            },
            parentTab: function(title, url) {
                var dataId = window.frameElement.getAttribute('data-id');
                createMenuItem(url, title);
                closeItem(dataId)
            },
            closeTab: function(dataId) {
                closeItem(dataId)
            },
            disable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled")
            },
            enable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled")
            },
            loading: function(message) {
                $.blockUI({
                    message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>'
                })
            },
            closeLoading: function() {
                setTimeout(function() {
                    $.unblockUI()
                }, 50)
            },
            reload: function() {
                parent.location.reload()
            }
        },
        operate: {
            submit: function(url, type, dataType, data, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    beforeSend: function() {
                        $.modal.loading("正在处理中，请稍后...")
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result)
                        }
                        $.operate.ajaxSuccess(result)
                    }
                };
                $.ajax(config)
            },
            post: function(url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback)
            },
            put: function(url, data, callback) {
                $.operate.submit(url, "put", "json", data, callback)
            },
            get: function(url, callback) {
                $.operate.submit(url, "get", "json", "", callback)
            },
            delete: function(url, callback) {
                $.operate.submit(url, "delete", "json", "", callback)
            },
            detail: function(id, width, height) {
                table.set();
                var _url = $.operate.detailUrl(id);
                var _width = $.common.isEmpty(width) ? "800" : width;
                var _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
                    _width = 'auto';
                    _height = 'auto'
                }
                var options = {
                    title: table.options.modalName + "详细",
                    width: _width,
                    height: _height,
                    url: _url,
                    skin: 'layui-layer-gray',
                    btn: ['关闭'],
                    yes: function(index, layero) {
                        layer.close(index)
                    }
                };
                $.modal.openOptions(options)
            },
            detailUrl: function(id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.detailUrl.replace("{id}", id)
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return
                    }
                    url = table.options.detailUrl.replace("{id}", id)
                }
                return url
            },
            remove: function(id) {
                table.set();
                $.modal.confirm("确定删除该条" + table.options.modalName + "信息吗？", function() {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if (table.options.type == table_type.bootstrapTreeTable) {
                        $.operate.delete(url)
                    } else {
                        var data = {
                            "ids": id
                        };
                        $.operate.submit(url, "delete", "json", data)
                    }
                })
            },
            removeAll: function() {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function() {
                    var url = table.options.removeUrl;
                    var data = {
                        "ids": rows.join()
                    };
                    $.operate.submit(url, "delete", "json", data)
                })
            },
            clean: function(formId) {
                table.set();
                $.modal.confirm("确定清除当前过滤条件的日志吗？", function() {
                    var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                    $.modal.loading("正在操作，请稍后...");
                    $.delete(table.options.cleanUrl, $("#" + currentId).serializeArray(), function(result) {
                        $.modal.closeLoading();
                        $.modal.alertSuccess(result.msg);
                        $.form.reset()
                    })
                })
            },
            add: function(id) {
                table.set();
                $.modal.open("添加" + table.options.modalName, $.operate.addUrl(id))
            },
            addTab: function(id) {
                table.set();
                $.modal.openTab("添加" + table.options.modalName, $.operate.addUrl(id))
            },
            addFull: function(id) {
                table.set();
                var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
                $.modal.openFull("添加" + table.options.modalName, url)
            },
            addUrl: function(id) {
                var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url
            },
            edit: function(id) {
                table.set();
                if ($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
                    var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return
                    }
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("修改" + table.options.modalName, url)
                } else {
                    $.modal.open("修改" + table.options.modalName, $.operate.editUrl(id))
                }
            },
            editTab: function(id) {
                table.set();
                $.modal.openTab("修改" + table.options.modalName, $.operate.editUrl(id))
            },
            editFull: function(id) {
                table.set();
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id)
                } else {
                    var row = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    url = table.options.updateUrl.replace("{id}", row)
                }
                $.modal.openFull("修改" + table.options.modalName, url)
            },
            editUrl: function(id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id)
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return
                    }
                    url = table.options.updateUrl.replace("{id}", id)
                }
                return url
            },
            save: function(url, data, callback, method) {
                var config = {
                    url: url,
                    type: method || "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function() {
                        $.modal.loading("正在处理中，请稍后...");
                        $.modal.disable()
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result)
                        }
                        $.operate.successCallback(result)
                    }
                };
                $.ajax(config)
            },
            saveModal: function(url, data, callback, method) {
                var config = {
                    url: url,
                    type: method || "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function() {
                        $.modal.loading("正在处理中，请稍后...")
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result)
                        }
                        if (result.code == web_status.SUCCESS) {
                            $.modal.alertSuccess(result.msg)
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg)
                        }
                        $.modal.closeLoading()
                    }
                };
                $.ajax(config)
            },
            saveTab: function(url, data, callback, method) {
                var config = {
                    url: url,
                    type: method || "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function() {
                        $.modal.loading("正在处理中，请稍后...")
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result)
                        }
                        $.operate.successTabCallback(result)
                    }
                };
                $.ajax(config)
            },
            ajaxSuccess: function(result) {
                if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh()
                } else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                    $.modal.msgSuccess(result.msg);
                    $.treeTable.refresh()
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg)
                }
                $.modal.closeLoading()
            },
            saveSuccess: function(result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS)
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg)
                }
                $.modal.closeLoading()
            },
            successCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                    var parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.table.refresh()
                    } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.treeTable.refresh()
                    } else {
                        $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS)
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg)
                }
                $.modal.closeLoading();
                $.modal.enable()
            },
            successTabCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                    var topWindow = $(window.parent.document);
                    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
                    var $contentWindow = $('.mockFrame[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
                    $.modal.close();
                    $contentWindow.$.modal.msgSuccess(result.msg);
                    $contentWindow.$(".layui-layer-padding").removeAttr("style");
                    if ($contentWindow.table.options.type == table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh()
                    } else if ($contentWindow.table.options.type == table_type.bootstrapTreeTable) {
                        $contentWindow.$.treeTable.refresh()
                    }
                    $.modal.closeTab()
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg)
                }
                $.modal.closeLoading()
            }
        },
        validate: {
            unique: function(value) {
                if (value == "0" || value === true || value === "true") {
                    return true
                }
                return false
            },
            form: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().form()
            },
            reset: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().resetForm()
            }
        },
        tree: {
            _option: {},
            _lastValue: {},
            init: function(options) {
                var defaults = {
                    id: "tree",
                    expandLevel: 0,
                    view: {
                        selectedMulti: false,
                        nameIsHTML: true
                    },
                    check: {
                        enable: false,
                        nocheckInherit: true,
                    },
                    data: {
                        key: {
                            title: "title"
                        },
                        simpleData: {
                            enable: true
                        }
                    },
                };
                var options = $.extend(defaults, options);
                $.tree._option = options;
                var setting = {
                    callback: {
                        onClick: options.onClick,
                        onCheck: options.onCheck,
                        onDblClick: options.onDblClick
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                $.get(options.url, function(data) {
                    var treeId = $("#treeId").val();
                    tree = $.fn.zTree.init($("#" + options.id), setting, data);
                    $._tree = tree;
                    var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                    for (var i = 0; i < nodes.length; i++) {
                        tree.expandNode(nodes[i], true, false, false)
                    }
                    var node = tree.getNodesByParam("id", treeId, null)[0];
                    $.tree.selectByIdName(treeId, node)
                })
            },
            searchNode: function() {
                var value = $.common.trim($("#keyword").val());
                if ($.tree._lastValue == value) {
                    return
                }
                $.tree._lastValue = value;
                var nodes = $._tree.getNodes();
                if (value == "") {
                    $.tree.showAllNode(nodes);
                    return
                }
                $.tree.hideAllNode(nodes);
                $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value))
            },
            selectByIdName: function(treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.selectNode(node, true)
                }
            },
            showAllNode: function(nodes) {
                nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._tree.expandNode(nodes[i], true, false, false, false)
                    } else {
                        $._tree.expandNode(nodes[i], true, true, false, false)
                    }
                    $._tree.showNode(nodes[i]);
                    $.tree.showAllNode(nodes[i].children)
                }
            },
            hideAllNode: function(nodes) {
                var tree = $.fn.zTree.getZTreeObj("tree");
                var nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._tree.hideNode(nodes[i])
                }
            },
            showParent: function(treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._tree.showNode(parentNode);
                    $._tree.expandNode(parentNode, true, false, false);
                    treeNode = parentNode
                }
            },
            showChildren: function(treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._tree.showNode(node);
                        $.tree.showChildren(node)
                    }
                }
            },
            updateNodes: function(nodeList) {
                $._tree.showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.tree.showChildren(treeNode);
                    $.tree.showParent(treeNode)
                }
            },
            getCheckedNodes: function(column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function(row) {
                    return row[_column]
                }).join()
            },
            notAllowParents: function(_tree) {
                var nodes = _tree.getSelectedNodes();
                if (nodes.length == 0) {
                    $.modal.msgError("请选择节点后提交");
                    return false
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                        return false
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                        return false
                    }
                }
                return true
            },
            notAllowLastLevel: function(_tree) {
                var nodes = _tree.getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                        return false
                    }
                }
                return true
            },
            toggleSearch: function() {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus()
            },
            collapse: function() {
                $._tree.expandAll(false)
            },
            expand: function() {
                $._tree.expandAll(true)
            }
        },
        common: {
            isEmpty: function(value) {
                if (value == null || this.trim(value) == "") {
                    return true
                }
                return false
            },
            isNotEmpty: function(value) {
                return !$.common.isEmpty(value)
            },
            nullToStr: function(value) {
                if ($.common.isEmpty(value)) {
                    return "-"
                }
                return value
            },
            visible: function(value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true
                }
                return false
            },
            trim: function(value) {
                if (value == null) {
                    return ""
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "")
            },
            equals: function(str, that) {
                return str == that
            },
            equalsIgnoreCase: function(str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase()
            },
            split: function(str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value
            },
            sprintf: function(str) {
                var args = arguments,
                    flag = true,
                    i = 1;
                str = str.replace(/%s/g, function() {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return ''
                    }
                    return arg
                });
                return flag ? str : ''
            },
            random: function(min, max) {
                return Math.floor((Math.random() * max) + min)
            },
            startWith: function(value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            endWith: function(value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            uniqueFn: function(array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i])
                    }
                }
                return result
            },
            join: function(array, separator) {
                if ($.common.isEmpty(array)) {
                    return null
                }
                return array.join(separator)
            },
            formToJSON: function(formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function(i, field) {
                    if (json[field.name]) {
                        json[field.name] += ("," + field.value)
                    } else {
                        json[field.name] = field.value
                    }
                });
                return json
            },
            getLength: function(obj) {
                var count = 0;
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++
                    }
                }
                return count
            }
        }
    })
})(jQuery);
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};
web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301
};
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};
$.date = function(dateObject) {
    if (!dateObject) {
        return ""
    }
    var d = new Date(dateObject);
    var day = d.getDate();
    var month = d.getMonth() + 1;
    var year = d.getFullYear();
    var hour = d.getHours();
    hour = hour < 10 ? "0" + hour : hour;
    var min = d.getMinutes();
    min = min < 10 ? "0" + min : min;
    var sec = d.getSeconds();
    sec = sec < 10 ? "0" + sec : sec;
    var date = `${year}/${month}/${day}${hour}:${min}:${sec}`;
    return date
};