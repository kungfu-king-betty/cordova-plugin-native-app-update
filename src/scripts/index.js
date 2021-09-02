var plugin = function () {
    return window.AppUpdate;
};
var AppUpdate = /** @class */ (function () {
    function AppUpdate() {
    }
    AppUpdate.needsUpdate = function (success, failure) {
        var checker = plugin();
        return checker.needsUpdate.apply(checker, arguments);
    };
    return AppUpdate;
}());
export default AppUpdate;
