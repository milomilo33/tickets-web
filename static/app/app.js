const MainView = {template: '<mainview></mainview>'}
const RegistrationView = {template: '<registrationview></registrationview>'}
const LoginView = {template: '<loginview></loginview>'}
const BuyerView = {template: '<buyerview></buyerview>'}
const ProfileView = {template: '<profileview></profileview>'}
const AllManifestationsView = {template: '<allmanifestationsview></allmanifestationsview>'}
const ManifestationDetailsView = {template: '<manifestationdetailsview></manifestationdetailsview>'}
const SellerView = {template: '<sellerview></sellerview>'}
const AdminView = {template: '<adminview></adminview>'}
const CreateManifestation = {template: '<createmanifestationview></createmanifestationview>'}
const AllTicketsView = {template: '<allticketsview></allticketsview>'}


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
                },
                {
                    path: '',
                    component: AllManifestationsView
                },
                {
                    path: 'ManifestationDetails',
                    component: ManifestationDetailsView
                },
            ]
        },
        {
            path: '/BuyerView',
            component: BuyerView,
            children: [
                {
                    path: '',
                    component: AllManifestationsView
                },
                {
                    path: 'ManifestationDetails',
                    component: ManifestationDetailsView
                },
                {
                    path: 'Profile',
                    component: ProfileView
                },
                {
                    path: 'AllTickets',
                    component: AllTicketsView
                }
            ]
        },
        {
            path: '/SellerView',
            component: SellerView,
            children: [
                {
                    path: 'Profile',
                    component: ProfileView
                },
                {
                    path: '',
                    component: AllManifestationsView
                },
                {
                    path: 'ManifestationDetails',
                    component: ManifestationDetailsView
                },
                {
                    path: 'CreateManifestation',
                    component: CreateManifestation
                },
                {
                    path: 'AllTickets',
                    component: AllTicketsView
                }
            ]
        },
        {
            path: '/AdminView',
            component: AdminView,
            children: [
                {
                    path: 'Profile',
                    component: ProfileView
                },
                {
                    path: '',
                    component: AllManifestationsView

                },
                {
                    path: 'ManifestationDetails',
                    component: ManifestationDetailsView
                },
                {
                    path: 'AllTickets',
                    component: AllTicketsView
                }
            ]
        },
    ]
});


var app = new Vue({
    router,
    el: '#application'
});