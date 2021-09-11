Vue.component('profileview', {
    template: `
        <div class="container">
            <b-alert v-model="showSuccessAlert" dismissible fade variant="success">
              You have successfully updated your profile.
            </b-alert>
            <b-alert v-model="showFailedAlert" dismissible fade variant="danger">
                {{this.errorMessage}}
            </b-alert>
            <div class="row justify-content-center">
              <div class="col-md-8">
                <div class="card">
                  <div class="card-header">Profile</div>
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
                      
                      <div class="form-group row" v-if="isBuyer">
                        <label
                          for="points"
                          class="col-md-4 col-form-label text-md-right"
                          >Points</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="points"
                            class="form-control"
                            name="points"
                            v-model="points"
                            readonly
                          />
                        </div>
                      </div>
                      
                      <div class="form-group row" v-if="isBuyer">
                        <label
                          for="userType"
                          class="col-md-4 col-form-label text-md-right"
                          >Account type</label
                        >
                        <div class="col-md-4">
                          <input
                            type="text"
                            id="userType"
                            class="form-control"
                            name="userType"
                            v-model="userType"
                            readonly
                          />
                        </div>
                      </div>
            
                      <div class="col-md-4 offset-md-4">
                        <button class="btn btn-success">Update</button>
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
            errorMessage: "",
            showSuccessAlert: false,
            showFailedAlert: false,
            isBuyer: false,
            points: "",
            userType: ""
        };
    },

    methods: {
        formSubmit(e) {
            e.preventDefault();

            let _this = this;
            if (
                !this.name || this.name.trim() === "" ||
                !this.lastName || this.lastName.trim() === "" ||
                !this.gender || this.gender.trim() === "" ||
                !this.dateOfBirth
            ) {
                this.errorMessage = "Some fields were left blank!";
                this.showFailedAlert = true;
                return;
            }

            axios
                .post('/users/profile', {
                    name: this.name,
                    surname: this.lastName,
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
    },

    mounted() {
        axios.get('/user')
            .then(response => {
                let user = response.data;

                this.name = user.name;
                this.lastName = user.surname;
                this.gender = user.gender;
                this.dateOfBirth = new Date(user.birth).toISOString().split('T', 1)[0];

                if (user.role === "KUPAC") {
                    this.points = user.points;
                    if(user.type)
                        this.userType = user.type.name;
                    else this.userType = "None";
                    this.isBuyer = true;
                }
            })
            .catch(error => console.log(error));
    }
})