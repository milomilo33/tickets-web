Vue.component('registrationview', {
    template: `
        <div class="container">
            <b-alert v-model="showSuccessAlert" dismissible fade variant="success">
              You have successfully registered as '{{this.username}}'.
            </b-alert>
            <b-alert v-model="showFailedAlert" dismissible fade variant="danger">
                {{this.errorMessage}}
            </b-alert>
            <div class="row justify-content-center">
              <div class="col-md-8">
                <div class="card">
                  <div class="card-header">Registration form</div>
                  <div class="card-body">
                    <form name="my-form" @submit="formSubmit">
                      <div class="form-group row">
                        <label for="name" class="col-md-4 col-form-label text-md-right"
                          >First Name</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="name"
                            class="form-control"
                            name="name"
                            v-model="name"
                          />
                        </div>
                      </div>
            
                      <div class="form-group row">
                        <label
                          for="lastName"
                          class="col-md-4 col-form-label text-md-right"
                          >Last Name</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="lastName"
                            class="form-control"
                            name="lastName"
                            v-model="lastName"
                          />
                        </div>
                      </div>
                      <div class="form-group row">
                        <label
                          for="gender"
                          class="col-md-4 col-form-label text-md-right"
                          >Gender</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="gender"
                            class="form-control"
                            name="gender"
                            v-model="gender"
                          />
                        </div>
                      </div>
                      
                      <div class="form-group row">
                        <label
                          for="dateOfBirth"
                          class="col-md-4 col-form-label text-md-right"
                          >Date of birth</label
                        >
                        <div class="col-md-4">
                            <input 
                                type="date"
                                id="dateOfBirth"
                                class="form-control"
                                name="dateOfBirth"
                                v-model="dateOfBirth" 
                                :max="currentDate"
                            />
                        </div>
                      </div>
                      
                      <div class="form-group row">
                        <label
                          for="username"
                          class="col-md-4 col-form-label text-md-right"
                          >Username</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="username"
                            class="form-control"
                            name="username"
                            v-model="username"
                          />
                        </div>
                      </div>
            
                      <div class="form-group row">
                        <label
                          for="password1"
                          class="col-md-4 col-form-label text-md-right"
                          >Password</label
                        >
                        <div class="col-md-4">
                          <input
                            type="password"
                            id="password1"
                            class="form-control"
                            name="password1"
                            v-model="password1"
                          />
                        </div>
                      </div>
                      <div class="form-group row">
                        <label
                          for="password2"
                          class="col-md-4 col-form-label text-md-right"
                          >Confirm password</label
                        >
                        <div class="col-md-4">
                          <input
                            type="password"
                            id="password2"
                            class="form-control"
                            name="password2"
                            v-model="password2"
                          />
                        </div>
                      </div>
            
                      <div class="col-md-4 offset-md-4">
                        <button class="btn btn-success">Submit</button>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
            </div>
    `,
    data() {
        return {
            name: "",
            lastName: "",
            gender: "",
            dateOfBirth: "",
            currentDate: new Date().toISOString().split('T', 1)[0],
            username: "",
            password1: "",
            password2: "",
            errorMessage: "",
            showSuccessAlert: false,
            showFailedAlert: false,
        };
    },
    methods: {
        formSubmit(e) {
            e.preventDefault();

            let errorFound = false;
            let _this = this;
            if (
                !this.name ||
                !this.lastName ||
                !this.gender ||
                !this.dateOfBirth ||
                !this.username ||
                !this.password1 ||
                !this.password2
            ) {
                errorFound = true;
                this.errorMessage = "Some fields were left blank!";
                this.showFailedAlert = true;
                return;
            }
            if (this.password2 !== this.password1) {
                errorFound = true;
                this.errorMessage = "The passwords do not match!";
                this.showFailedAlert = true;
                return;
            }

            if (!errorFound) {
                axios
                    .post('/', {
                        name: this.name,
                        surname: this.lastName,
                        username: this.username,
                        password: this.password2,
                        gender: this.gender,
                        birth: this.dateOfBirth
                    })
                    .then(function (response) {
                        _this.showSuccessAlert = true;
                    })
                    .catch(function (error) {
                        console.log(error);
                        _this.errorMessage = error.response.data;
                        _this.showFailedAlert = true;
                    });
            }
        }
    }
})