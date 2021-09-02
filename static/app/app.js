const MainView = {template: '<mainview></mainview>'}


axios.defaults.baseURL = 'http://localhost:8080/api/'
const router = new VueRouter({
    mode: 'hash',
    routes: [
        {path: '/', component: MainView},
    ]
});


var app = new Vue({
    router,
    el: '#application'
});