Vue.component('mainview', {
    template: `
        <div>
            <div class="navbar-div" style="margin-bottom: 100px;">
                <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
                    <b-navbar-brand href="/">Home Page</b-navbar-brand>
                    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
                    <b-collapse id="nav-collapse" is-nav>
                    <b-navbar-nav>
                      <b-nav-item href="">Placeholder #1</b-nav-item>
                      <b-nav-item href="">Placeholder #2</b-nav-item>
                        <b-nav-item-dropdown text="Dropdown placeholder" left>
                            <b-dropdown-item href="">Option uno</b-dropdown-item>
                            <b-dropdown-item href="">Option dos</b-dropdown-item>
                            <b-dropdown-item href="">Option tres</b-dropdown-item>
                            <b-dropdown-item href="">Option cuatro</b-dropdown-item>
                        </b-nav-item-dropdown>
                      <b-nav-item href="">Placeholder #3</b-nav-item>
                    </b-navbar-nav>
                    
                    <!-- Right aligned nav items -->
                    <b-navbar-nav class="ml-auto">                
<!--                        <b-nav-item-dropdown text="User" right>-->
<!--                            <b-dropdown-item href="">Profile</b-dropdown-item>-->
<!--                            <b-dropdown-item href="">Change password</b-dropdown-item>-->
<!--                            <b-dropdown-item href="">Sign Out</b-dropdown-item>-->
<!--                        </b-nav-item-dropdown>-->

                        <b-nav-item href="">Login</b-nav-item>
                        <b-nav-item href="/#/Registration">Register</b-nav-item>
                    </b-navbar-nav>
                    </b-collapse>
                </b-navbar>
            </div>
            
            <router-view></router-view>
            
            <footer>
                <div class="container-fluid">
                    <div class="row text-center">
                        <div class=col-md-6>
                            <hr class="light">
                            <h5>Tim: ROBOT</h5>
                            <hr class="light">
                            <p>Joca Petljanski</p>
                            <p>Milo Milovanovic</p>
                        </div>
                        <div class=col-md-6>
                            <hr class="light">
                            <h5>Contact</h5>
                            <hr class="light">
                            <p>joksip284@gmail.com (ili kako vec)</p>
                            <p>milovanovicm309@gmail.com</p>
                        </div>
                    </div>
                </div>
            </footer>
        </div>
    `
    }
);