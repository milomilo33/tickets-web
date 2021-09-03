const MainView = {template: '<mainview></mainview>'}
const RegistrationView = {template: '<registrationview></registrationview>'}


axios.defaults.baseURL = 'http://localhost:8080/api/'
const router = new VueRouter({
    mode: 'hash',
    routes: [
        {path: '/', component: MainView, children: [
                {
                    path: 'Registration',
                    component: RegistrationView
                }
            ]},
    ]
});


var app = new Vue({
    router,
    el: '#application'
});