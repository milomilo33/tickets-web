Vue.component('buyerview', {
        data() {
            return {
                currentUsername: ""
            };
        },

        template: `
            <div>
                <div class="navbar-div" style="margin-bottom: 100px;">
                    <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
                        <b-navbar-brand href="/#/BuyerView">Home Page</b-navbar-brand>
                        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                        <b-collapse id="nav-collapse" is-nav>
                        <b-navbar-nav>
                          <b-nav-item href="/#/BuyerView/AllTickets">Your tickets</b-nav-item>                         
                        </b-navbar-nav>
                        
                        <!-- Right aligned nav items -->
                        <b-navbar-nav class="ml-auto">                
                            <b-nav-item-dropdown :text="currentUsername" right>
                                <b-dropdown-item href="/#/BuyerView/Profile">Profile</b-dropdown-item>
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