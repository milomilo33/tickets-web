Vue.component('loginview', {
   template: `
        <div class="justify-content-center login">
          <b-alert v-model="showErrorAlert" dismissible fade variant="danger">
              {{this.errorMessage}}
          </b-alert>
          <b-card title="Login">
            <b-form>
              <b-form-input
                id="input-1"
                v-model="username"
                placeholder="Username"
                required
              >
              </b-form-input>
                <br/>
              <b-form-input
                id="input-2"
                v-model="password"
                placeholder="Password"
                required
                type="password"
              >
              </b-form-input>
              <div class="mt-2">
                <b-button variant="primary" type="button" v-on:click="login"
                  >Login</b-button
                >
              </div>
            </b-form>
          </b-card>
        </div>
   `,

    data() {
        return {
            username: "",
            password: "",
            showErrorAlert: false,
            errorMessage: ""
        };
    },

    methods: {
        login(e) {
            e.preventDefault();

            let _this = this;
            if (!this.username || this.username.trim() === "" || !this.password || this.password.trim() === "") {
                this.errorMessage = "Invalid username and/or password!";
                this.showErrorAlert = true;
                return;
            }
            axios
                .post("/login", {
                    username: this.username,
                    password: this.password,
                })
                .then((response) => {
                    console.log(response);
                    let role = response.data;
                    if (role === "buyer")
                        _this.$router.push("/BuyerView");
                    else if (role === "seller")
                        _this.$router.push("/SellerView");
                    else if (role === "administrator")
                        _this.$router.push("/AdminView");
                    else
                        console.log("Invalid role returned from server?");
                })
                .catch((error) => {
                    console.log(error.response.data);
                    this.errorMessage = error.response.data;
                    _this.showErrorAlert = true;
                });
        },
    }
});