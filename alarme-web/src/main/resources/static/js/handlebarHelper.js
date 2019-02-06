(function (Handlebars){
    Handlebars.registerHelper('json', function(obj) {
       try{
            return JSON.stringify(obj)
       }catch (e){
            return '';
       }
    });
    Handlebars.registerHelper('eq', function(a,b, option) {
        return (a == b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('ne', function(a,b, option) {
        return (a != b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('gt', function(a,b, option) {
        return (a > b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('gte', function(a,b, option) {
        return (a >= b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('lt', function(a,b, option) {
        return (a < b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('lte', function(a,b, option) {
        return (a <= b) ? option.fn(this) : option.inverse(this);
    });
    Handlebars.registerHelper('ifCond', function (v1, operator, v2, options) {
        switch (operator) {
            case '==':
                return (v1 == v2) ? options.fn(this) : options.inverse(this);
            case '===':
                return (v1 === v2) ? options.fn(this) : options.inverse(this);
            case '!=':
                return (v1 != v2) ? options.fn(this) : options.inverse(this);
            case '!==':
                return (v1 !== v2) ? options.fn(this) : options.inverse(this);
            case '<':
                return (v1 < v2) ? options.fn(this) : options.inverse(this);
            case '<=':
                return (v1 <= v2) ? options.fn(this) : options.inverse(this);
            case '>':
                return (v1 > v2) ? options.fn(this) : options.inverse(this);
            case '>=':
                return (v1 >= v2) ? options.fn(this) : options.inverse(this);
            case '&&':
                return (v1 && v2) ? options.fn(this) : options.inverse(this);
            case '||':
                return (v1 || v2) ? options.fn(this) : options.inverse(this);
            default:
                return options.inverse(this);
        }
    });

}) (Handlebars || {});
