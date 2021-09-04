const MainView = {template: '<mainview></mainview>'}
const RegistrationView = {template: '<registrationview></registrationview>'}
const LoginView = {template: '<loginview></loginview>'}
const BuyerView = {template: '<buyerview></buyerview>'}
const ProfileView = {template: '<profileview></profileview>'}


axios.defaults.baseURL = 'http://localhost:8080/api/'
const router = new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: '/',
            component: MainView,
            children: [
                {
                    path: 'Registration',
                    component: RegistrationView
                },
                {
                    path: 'Login',
                    component: LoginView
                }
            ]
        },

        {
            path: '/BuyerView',
            component: BuyerView,
            children: [
                {
                    path: 'Profile',
                    component: ProfileView
                }
            ]
        }
    ]
});


var app = new Vue({
    router,
    el: '#application'
});