Vue.component('mainview', {
    template: `
        <div>
            <div class="navbar-div" style="margin-bottom: 100px;">
                <b-navbar fixed="top" toggleable="lg" type="light" variant="light">
                    <b-navbar-brand href="/">Home Page</b-navbar-brand>                
                    </b-navbar-nav>
                    
                    <!-- Right aligned nav items -->
                    <b-navbar-nav class="ml-auto">                
<!--                        <b-nav-item-dropdown text="User" right>-->
<!--                            <b-dropdown-item href="">Profile</b-dropdown-item>-->
<!--                            <b-dropdown-item href="">Change password</b-dropdown-item>-->
<!--                            <b-dropdown-item href="">Sign Out</b-dropdown-item>-->
<!--                        </b-nav-item-dropdown>-->

                        <b-nav-item href="/#/Login">Login</b-nav-item>
                        <b-nav-item href="/#/Registration">Register</b-nav-item>
                    </b-navbar-nav>
                    </b-collapse>
                </b-navbar>
            </div>
            
            <router-view></router-view>
            
            
        </div>
    `
    }
);