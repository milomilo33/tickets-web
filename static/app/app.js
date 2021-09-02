const HelloWorldView = {template: '<helloworldview></helloworldview>'}


axios.defaults.baseURL = 'http://localhost:8080/api/'
const router = new VueRouter({
    mode: 'hash',
    routes: [
        {path: '/', component: HelloWorldView},
    ]
});


var app = new Vue({
    router,
    el: '#application'
});