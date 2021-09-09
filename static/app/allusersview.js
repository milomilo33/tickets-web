Vue.component('allusersview', {
        data(){
            return{
                users: [],
                name: '',
                lastName: '',
                username: '',
                sortOptions: [
                    {value: 0, text: 'Sort by:'},
                    {value: 1, text: 'Name (Descending)'},
                    {value: 2, text: 'Name (Ascending)'},
                    {value: 3, text: 'Last Name (Descending)'},
                    {value: 4, text: 'Last Name (Ascending)'},
                    {value: 5, text: 'Username (Descending)'},
                    {value: 6, text: 'Username (Ascending)'},
                    {value: 7, text: 'Points (Descending)'},
                    {value: 8, text: 'Points (Ascending)'}
                ],
                sortSelected: 0,
                userRoles: [
                    {value: 'Select a user role:', text: 'Select a user role:'},
                    {value: 'Buyer', text: 'Buyer'},
                    {value: 'Seller', text: 'Seller'},
                    {value: 'Administrator', text: 'Administrator'}
                ],
                userRoleSelected: 'Select a user role:',
                userTypes: [],
                userTypeSelected: 'Select a user type:',
                onlySuspicious: '',
                role: '',
                errorMessage: ''
            };
        },

        template: `
        <div>
        <b-form class="px-3" inline>
            <b-form-group label="Search options:" label-size="lg" label-class="font-weight-bold">
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="user-search-name" placeholder="Name" v-model="name"/>
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="user-search-last-name" placeholder="Last Name" v-model="lastName"/>
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="user-search-username" placeholder="Username" v-model="username"/>
                <b-form-select v-model="sortSelected" :options="sortOptions" class="mb-2 mr-sm-2 mb-sm-0"></b-form-select>
                <b-button class="mb-2 mr-sm-2 mb-sm-0" v-on:click="search">Search</b-button>
            </b-form-group>
        </b-form>
        <b-form class="px-3" inline>
            <b-form-group label="Filters: " label-class="font-weight-bold">
                <b-form-select v-if="isAdmin()" class="mb-2 mr-sm-2 mb-sm-0" :options="userRoles" v-model="userRoleSelected"></b-form-select>
                <b-form-select class="mb-2 mr-sm-2 mb-sm-0" :options="userTypes" v-model="userTypeSelected"></b-form-select>
            </b-form-group>
            <b-form-checkbox v-model="onlySuspicious" class="pt-4" v-if="this.role === 'admin'">Show suspicious users</b-form-checkbox>
        </b-form>
        <br/>
        <b-card-group deck>
        <div class="row pl-3">
        <div class="col" v-for="u in users" :key="u.id" >
          <b-card
            img-src="img/user.png"
            img-alt="Image"
            img-top
            tag="article"
            style="max-width: 20rem;"
            class="mb-2"
          >
            <b-list-group flush>
              <b-list-group-item>Name: {{u.name}}</b-list-group-item>
              <b-list-group-item>Last name: {{u.surname}}</b-list-group-item>
              <b-list-group-item>Username: {{u.username}}</b-list-group-item>
              <b-list-group-item>Gender: {{u.gender}}</b-list-group-item>
              <b-list-group-item>Date of birth: {{u.birth}}</b-list-group-item>
              <b-list-group-item v-if="isAdmin()">Role: {{userRoleConv(u.role)}}</b-list-group-item>
              <b-list-group-item v-if="u.role === 'KUPAC'">Type: {{u.type.name}}</b-list-group-item>
              <b-list-group-item v-if="u.role === 'KUPAC'">Points: {{u.points}}</b-list-group-item>
              <b-list-group-item v-if="u.blocked">BLOCKED</b-list-group-item>
            </b-list-group>
        
            <b-button v-if="showBlock(u)" v-on:click="blockUser(u.id)" variant="warning">Block</b-button>
            <b-button v-if="showDelete(u)" v-on:click="deleteUser(u.id)" variant="danger">Delete</b-button>
          </b-card>
        </div>        
        </div>
        </b-card-group>
        
             <b-modal ref="error-modal" hide-footer title="Error">
                <div class="d-block text-center">
                    <p>{{ this.errorMessage }}</p>
                </div>
                <b-button class="mt-3" variant="outline-danger" block @click="hideErrorModal">Close</b-button>
            </b-modal>
        
        </div>
    `,

        methods:{
            userRoleConv(role){
                switch (role) {
                    case "KUPAC":
                        return "Buyer";
                    case "PRODAVAC":
                        return "Seller";
                    case "ADMINISTRATOR":
                        return "Administrator";
                    default:
                        return "ERROR";
                }
            },
            showBlock(user){
                return !user.blocked && user.role === "KUPAC" && this.role === "admin";
            },
            showDelete(user){
                return this.role === "admin" && user.role !== "ADMINISTRATOR";
            },
            isAdmin(){
                return this.role === "admin";
            },
            search(e){
                e.preventDefault();
                let searchOptions = '?name=' + this.name + '&lastName=' + this.lastName + '&sortSelected=' + this.sortSelected +
                    '&onlySuspicious=' + this.onlySuspicious + '&userTypeSelected=' + this.userTypeSelected + '&userRoleSelected=' +
                    this.userRoleSelected + '&username=' + this.username;
                let self = this;
                axios.get('usersearch' + searchOptions)
                    .then(response => {
                        self.users = response.data;
                    })
                    .catch(error => console.log(error));
            },
            blockUser(id){
                let self = this;
                axios.get('blockuser/' + id)
                    .then(res => {
                        let username = res.data;
                        let okString = `You have blocked user '${username}'.`;
                        this.$bvModal.msgBoxOk(okString, {
                            title: 'Blocked',
                            size: 'sm',
                            buttonSize: 'sm',
                            okVariant: 'success',
                            headerClass: 'p-2 border-bottom-0',
                            footerClass: 'p-2 border-top-0',
                            centered: true
                        });

                        axios.get('/allusers')
                            .then(response => {
                                self.users = response.data;
                            })
                            .catch(error => console.log(error));
                    })
                    .catch(err => {
                        this.errorMessage = "User is not suspicious and therefore cannot be blocked!";
                        this.showErrorModal();
                    });
            },
            deleteUser(id){
                let self = this;
                axios.get('deleteuser/' + id)
                    .then(res => {
                        let username = res.data;
                        let okString = `You have deleted user '${username}'.`;
                        this.$bvModal.msgBoxOk(okString, {
                            title: 'Deleted',
                            size: 'sm',
                            buttonSize: 'sm',
                            okVariant: 'success',
                            headerClass: 'p-2 border-bottom-0',
                            footerClass: 'p-2 border-top-0',
                            centered: true
                        });

                        axios.get('/allusers')
                            .then(response => {
                                self.users = response.data;
                            })
                            .catch(error => console.log(error));
                    })
                    .catch(err => {
                        this.errorMessage = "Cannot delete user!";
                        this.showErrorModal();
                    });
            },
            hideErrorModal() {
                this.$refs['error-modal'].hide();
            },
            showErrorModal() {
                this.$refs['error-modal'].show();
            }
        },

        mounted() {
            let self = this;
            axios.get('/allusers')
                .then(response => {
                    self.users = response.data;
                })
                .catch(error => console.log(error));

            axios.get('/usertypes')
                .then(response => {
                    self.userTypes = response.data;
                    self.userTypes.unshift("Select a user type:")
                })
                .catch(error => console.log(error));

            if (this.$route.path.includes("AdminView"))
                this.role = "admin";
            else if ((this.$route.path.includes("SellerView")))
                this.role = "seller";
        },

    }
)