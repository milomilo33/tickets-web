Vue.component('registrationview', {
    template: `
        <div class="container">
            <b-alert v-model="showSuccessAlert" dismissible fade variant="success">
              Success!
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
            password1: "",
            password2: "",
            phoneNumber: "",
            number: 0,
            errorMessage: "",
            showSuccessAlert: false,
            showFailedAlert: false,
        };
    },
    methods: {
        formSubmit(e) {
            e.preventDefault();
        }
    }
})