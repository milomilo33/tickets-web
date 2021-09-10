Vue.component('adminview', {
        data() {
            return {
                currentUsername: ""
            };
        },

        template: `
            <div>
                <div class="navbar-div" style="margin-bottom: 100px;">
                    <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
                        <b-navbar-brand href="/#/AdminView">Home Page</b-navbar-brand>
                        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                        <b-collapse id="nav-collapse" is-nav>
                        <b-navbar-nav>
                          <b-nav-item href="/#/AdminView/AllUsers">Users</b-nav-item>
                          <b-nav-item href="/#/AdminView/AllTickets">Tickets</b-nav-item>
                          <b-nav-item href="/#/AdminView/RegisterSeller">New Seller</b-nav-item>
                        </b-navbar-nav>
                        
                        <!-- Right aligned nav items -->
                        <b-navbar-nav class="ml-auto">                
                            <b-nav-item-dropdown :text="currentUsername" right>
                                <b-dropdown-item href="/#/AdminView/Profile">Profile</b-dropdown-item>
                                <b-dropdown-item href="" v-on:click="logout">Sign Out</b-dropdown-item>
                            </b-nav-item-dropdown>
                        </b-navbar-nav>
                        </b-collapse>
                    </b-navbar>
                </div>
                
                <router-view></router-view>
                
                
            </div>
        `,
    methods:{
        logout(e){
            e.preventDefault();
            axios.post('/logout')
                .then(res =>{
                    window.location.href = "#/";
                })
        }
    },
        mounted() {
            axios.get('/currentUsername')
                .then(response => {
                    this.currentUsername = response.data;
                    console.log(response.data);
                })
                .catch(error => console.log(error));
        }
    }
);