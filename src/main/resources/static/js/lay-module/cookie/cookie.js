layui.define("jquery", function(exports) {
    let $ = layui.jquery,
        config = $.cookie = function (key, value, options) {
            if (arguments.length > 1 && !$.isFunction(value)) {
                options = $.extend({}, config.defaults, options);
                if (typeof options.expires === 'number') {
                    var days = options.expires, t = options.expires = new Date();
                    t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
                }
                return (document.cookie = [
                    _cookie.encode(key), '=', _cookie.stringifyCookieValue(value),
                    options.expires ? '; expires=' + options.expires.toUTCString() : '',
                    options.path    ? '; path=' + options.path : '; path=/',
                    options.domain  ? '; domain=' + options.domain : '',
                    options.secure  ? '; secure' : ''
                ].join(''));
            }

            var result = key ? undefined : {},
                cookies = document.cookie ? document.cookie.split('; ') : [],
                i = 0,
                l = cookies.length;

            for (; i < l; i++) {
                var parts = cookies[i].split('='),
                    name = _cookie.decode(parts.shift()),
                    cookie = parts.join('=');
                if (key === name) {
                    result = _cookie.read(cookie, value);
                    break;
                }
                if (!key && (cookie = _cookie.read(cookie)) !== undefined) {
                    result[name] = cookie;
                }
            }
            return result;
        };

    config.defaults = {};

    var _cookie = {
        encode: function(string){
            return config.raw ? string : encodeURIComponent(string);
        },
        decode: function(string){
            return config.raw ? string : decodeURIComponent(string);
        },
        stringifyCookieValue: function(value){
            return this.encode(config.json ? JSON.stringify(value) : String(value));
        },
        parseCookieValue: function(string){
            if (string.indexOf('"') === 0) {
                string = string.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
            }
            try {
                string = decodeURIComponent(string.replace(/\+/g, ' '));
                return config.json ? JSON.parse(string) : string;
            } catch(e) {}
        },
        read: function(string, converter){
            var value = config.raw ? string : this.parseCookieValue(string);
            return $.isFunction(converter) ? converter(value) : value;
        },
        set: function(key, value, options){
            if (arguments.length > 1 && !$.isFunction(value)) {
                $.cookie(key, value, options);
            } else {
                return $.cookie(key);
            }
        },
        remove: function(key, options){
            $.cookie(key, '', $.extend({}, options, { expires: -1 }));
            return !$.cookie(key);
        }
    };

    exports("cookie", _cookie);
});