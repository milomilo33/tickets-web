Vue.component('sellerview', {
        data() {
            return {
                currentUsername: ""
            };
        },

        template: `
            <div>
                <div class="navbar-div" style="margin-bottom: 100px;">
                    <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
                        <b-navbar-brand href="/#/SellerView">Home Page</b-navbar-brand>
                        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                        <b-collapse id="nav-collapse" is-nav>
                        <b-navbar-nav>
                          <b-nav-item href="/#/SellerView/CreateManifestation">New Manifestation</b-nav-item>
                          <b-nav-item href="/#/SellerView/AllTickets">Tickets</b-nav-item>
                            <b-nav-item-dropdown text="Dropdown placeholder" left>
                                <b-dropdown-item href="">Option uno</b-dropdown-item>
                                <b-dropdown-item href="">Option dos</b-dropdown-item>
                                <b-dropdown-item href="">Option tres</b-dropdown-item>
                                <b-dropdown-item href="">Option cuatro</b-dropdown-item>
                            </b-nav-item-dropdown>
                          <b-nav-item href="/#/SellerView/SellersManifestations">My Manifestations</b-nav-item>                            
                          <b-nav-item href="">Placeholder #3</b-nav-item>
                        </b-navbar-nav>
                        
                        <!-- Right aligned nav items -->
                        <b-navbar-nav class="ml-auto">                
                            <b-nav-item-dropdown :text="currentUsername" right>
                                <b-dropdown-item href="/#/SellerView/Profile">Profile</b-dropdown-item>
                                <b-dropdown-item href="">Change password</b-dropdown-item>
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