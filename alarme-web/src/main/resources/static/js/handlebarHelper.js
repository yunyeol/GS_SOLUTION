(function (handlebars){
    Handlebars.registerHelper('eq', function(a,b, option) {
        return (a == b) ? option.fn(this) : option.inverse(this);
    });

}) (handlebars || {});
