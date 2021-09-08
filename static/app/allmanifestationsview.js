Vue.component('allmanifestationsview', {

    data(){
        return{
            manifestations: {},
            dateFrom: '',
            dateTo: '',
            sortOptions: [
                {value: 0, text: 'Sort by:'},
                {value: 1, text: 'Name (Descending)'},
                {value: 2, text: 'Name (Ascending)'},
                {value: 3, text: 'Date (Descending)'},
                {value: 4, text: 'Date (Ascending)'},
                {value: 5, text: 'Ticket price (Descending)'},
                {value: 6, text: 'Ticket price (Ascending)'},
                {value: 7, text: 'Address (Descending)'},
                {value: 8, text: 'Address (Ascending)'}
            ],
            sortSelected: 0,
            soldOut: false,
            types: [],
            typeSelected: 'Select a type:',
            address: '',
            priceFrom: 0.0,
            priceTo: 999999.0,
            name: '',
            showDeleted: false,
            showActivated: false,
            showReserveTickets: false,
            errorMessage: '',
            quantityToReserve: 0,
            showInvalidQty: false,
            selectedManifestationId: -1,
            selectedManifestationName: '',
            ticketTypeSelected: 'Regular',
            ticketTypes: [
                {value: 'Regular', text: 'Regular'},
                {value: 'Fan pit', text: 'Fan pit'},
                {value: 'VIP', text: 'VIP'}
            ],
            totalPrice: ''
        };
    },

    template: `
        <div>
        <b-form class="px-3" inline>
        <b-form-group label="Search options:" label-size="lg" label-class="font-weight-bold">
            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="manif-search-name" placeholder="Name" v-model="name"/>
            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="manif-search-address" placeholder="Address" v-model="address"/>
            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="manif-search-price-from" placeholder="Price (From)" v-model="priceFrom"/>
            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="manif-search-price-to" placeholder="Price (To)" v-model="priceTo"/>
            <b-form-select v-model="sortSelected" :options="sortOptions" class="mb-2 mr-sm-2 mb-sm-0"></b-form-select>
            <b-button class="mb-2 mr-sm-2 mb-sm-0" v-on:click="search">Search</b-button>
        </b-form-group>
        </b-form>
        <b-form class="px-3" inline>
        <b-form-group label="Date (From): " label-class="font-weight-bold" class="mb-2 mr-sm-2 mb-sm-0">
            <input id="manif-search-date-from" type="date" placeholder="Date (From)" v-model="dateFrom" class="form-control"/>
        </b-form-group>
        <b-form-group label="Date (To): " label-class="font-weight-bold" class="mb-2 mr-sm-2 mb-sm-0">
            <input id="manif-search-date-from" type="date" placeholder="Date (From)" v-model="dateTo" class="form-control"/>
        </b-form-group>
        <b-form-group label="Filters: " label-class="font-weight-bold">
            <b-form-select class="mb-2 mr-sm-2 mb-sm-0" :options="types" v-model="typeSelected" placeholder="Select a type:"></b-form-select>
      
        </b-form-group>
        <b-form-checkbox v-model="soldOut" class="pt-4">Only show manifestations that haven't sold out</b-form-checkbox>
        
        </b-form>
        <br/>
        <b-card-group deck>
        <div class="row pl-3">
        <div class="col" v-for="m in manifestations" :key="m.id" >
          <b-card
            :title="m.name"
            :img-src="m.picture"
            img-alt="Image"
            img-top
            tag="article"
            style="max-width: 20rem;"
            class="mb-2"
          >
            <b-list-group flush>
              <b-list-group-item>Type: {{m.type}}</b-list-group-item>
              <b-list-group-item>Opens on: {{dateTimeToDate(m.date)}} at {{dateTimeToTime(m.date)}}</b-list-group-item>
              <b-list-group-item>Address: {{m.location.address}}</b-list-group-item>
              <b-list-group-item>Entrance ticket: {{m.ticketPrice}} RSD</b-list-group-item>
              <b-list-group-item>Rating: {{ratingConv(m.rating)}}</b-list-group-item>
            </b-list-group>
        
            <b-button v-on:click="details(m.id)" variant="primary">Details</b-button>
            <b-button v-if="showDeleted" v-on:click="deleteManif(m.id)" variant="danger">Delete</b-button>
            <b-button v-if="activateBtn(m.active)" v-on:click="activateManif(m.id)" variant="success">Activate</b-button>
            <b-button v-if="showReserveTickets" v-on:click="reserveTickets(m.id, m.name)" variant="success">Reserve tickets</b-button>
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
        
            <b-modal
              id="reserveModal"
              ref="reserveModal"
              title="Amount of tickets to reserve"
              @show="resetReserveModal"
              @ok="handleReserveModalOk"
            >
                <b-alert v-model="showInvalidQty" dismissible fade variant="danger">
                    {{this.errorMessage}}
                </b-alert>
              <form ref="reserveForm" @submit.stop.prevent="handleReserveFormSubmit">
                <b-form-group
                  label="Quantity"
                  label-for="quantityInput"
                >
                  <b-form-input
                    id="quantityInput"
                    v-model="quantityToReserve"
                    type="number"
                    min="1"
                    step="1"
                    required
                  ></b-form-input>
                </b-form-group>
                <b-form-group label="Ticket type: " label-class="font-weight-bold">
                    <b-form-select :options="ticketTypes" v-model="ticketTypeSelected"></b-form-select>
                </b-form-group>
              </form>
            </b-modal>
        
        </div>
    `,

    methods:{
        dateTimeToDate(dateTime){
            return dateTime.date.day + "." + dateTime.date.month + "." + dateTime.date.year + ".";
        },
        dateTimeToTime(dateTime){
            let mins = dateTime.time.minute;
            if(mins < 10) mins = "0" + mins.toString();
            return dateTime.time.hour + ":" + mins + "h";
        },
        ratingConv(rating){
            if(rating === 0)
                return "Not rated";
            else
                return rating + "/10";
        },
        search(e){
            e.preventDefault();
            let searchOptions = '?dateFrom=' + this.dateFrom + '&dateTo=' + this.dateTo + '&sortSelected=' + this.sortSelected +
            '&soldOut=' + this.soldOut + '&typeSelected=' + this.typeSelected + '&address=' + this.address + '&priceFrom=' +
                this.priceFrom + '&priceTo=' + this.priceTo + '&name=' + this.name + '&isSeller=false';
            let self = this;
            axios.get('manifestationsearch' + searchOptions)
                .then(response => {
                    self.manifestations = response.data;
                    for(item of self.manifestations){
                        item.picture = 'data:image/png;base64,' + item.picture;
                        //item.date = new Date(item.date);
                    }
                })
                .catch(error => console.log(error));
        },
        details(id){
            let curr = window.location.href;
            curr = curr.split('/')[4];
            this.$router.push({path: curr + '/ManifestationDetails', query: {'id' : id}});
        },
        activateBtn(activatedInfo){
            return !activatedInfo && this.showActivated;
        },
        deleteManif(id){
            let self = this;
            axios.post("/deletemanifestation/" + id)
                .then(res => {
                    alert("Manifestation deleted!");
                    axios.get('/allmanifestations')
                        .then(response => {
                            self.manifestations = response.data;
                            for(item of self.manifestations){
                                item.picture = 'data:image/png;base64,' + item.picture;
                                //item.date = new Date(item.date);
                            }
                        })
                        .catch(error => console.log(error));
                })
                .catch(err => console.log(err));
        },
        activateManif(id){
            let self = this;
            axios.post("/activatemanifestation/" + id)
                .then(res => {
                    alert("Manifestation activated!");
                    axios.get('/allmanifestations')
                        .then(response => {
                            self.manifestations = response.data;
                            for(item of self.manifestations){
                                item.picture = 'data:image/png;base64,' + item.picture;
                                //item.date = new Date(item.date);
                            }
                        })
                        .catch(error => console.log(error));
                })
                .catch(err => console.log(err));
        },
        ticketsLeft(id){
            return axios.get('remainingtickets/' + id)
                .then(res => {
                    return parseInt(res.data);
                })
                .catch(err => {
                    console.error(err);
                    return -1;
                });
        },
        reserveTickets(id, name){
            this.selectedManifestationId = id;
            this.selectedManifestationName = name;
            this.ticketsLeft(id)
                .then(amount => {
                    if (amount <= 0) {
                        this.errorMessage = "No tickets left for this manifestation.";
                        this.showErrorModal();
                        return;
                    }

                    this.$refs['reserveModal'].show();
                })
                .catch(err => console.log(err));
        },
        hideErrorModal() {
            this.$refs['error-modal'].hide();
        },
        showErrorModal() {
            this.$refs['error-modal'].show();
        },
        resetReserveModal() {
            this.quantityToReserve = 0;
            this.showInvalidQty = false;
        },
        handleReserveModalOk(e) {
            e.preventDefault();
            this.handleReserveFormSubmit();
        },
        handleReserveFormSubmit() {
            if (!(/^\d+$/.test(this.quantityToReserve)) || this.quantityToReserve <= 0) {
                this.errorMessage = "Invalid ticket quantity!";
                this.showInvalidQty = true;
                return;
            }

            let query = '?quantity=' + this.quantityToReserve + '&manifestationId=' + this.selectedManifestationId +
                        '&type=' + this.ticketTypeSelected;
            // open confirm modal
            axios.get('checkreservationquantityandprice' + query)
                .then(res => {
                    let totalPrice = res.data;
                    let confirmString = `The total price for ${this.quantityToReserve} '${this.ticketTypeSelected}' ticket(s)` +
                                        `for '${this.selectedManifestationName}' is ${totalPrice} RSD. Are you sure you want to make this reservation?`;
                    this.$bvModal.msgBoxConfirm(confirmString, {
                        title: 'Confirm reservation',
                        size: 'sm',
                        buttonSize: 'sm',
                        okVariant: 'danger',
                        okTitle: 'YES',
                        cancelTitle: 'NO',
                        footerClass: 'p-2',
                        hideHeaderClose: false,
                        centered: true
                    })
                        .then(value => {
                            if (value) {
                                axios.post('makereservation', {
                                    quantity: this.quantityToReserve,
                                    manifestationId: this.selectedManifestationId,
                                    type: this.ticketTypeSelected
                                })
                                    .then(res => {
                                        let okString = `You have successfully reserved ${this.quantityToReserve}` +
                                            ` '${this.ticketTypeSelected}' ticket(s) for '${this.selectedManifestationName}'` +
                                            ` and thereby gained ${res.data} points.`;
                                        this.$bvModal.msgBoxOk(okString, {
                                            title: 'Reserved',
                                            size: 'sm',
                                            buttonSize: 'sm',
                                            okVariant: 'success',
                                            headerClass: 'p-2 border-bottom-0',
                                            footerClass: 'p-2 border-top-0',
                                            centered: true
                                        });
                                    })
                                    .catch(err => console.log(err));
                            }
                        })
                        .catch(err => {
                            // An error occurred
                            console.log(err)
                        })

                    this.$nextTick(() => {
                        this.$refs['reserveModal'].hide();
                    })
                })
                .catch(err => {
                    this.errorMessage = "Not enough tickets left for this manifestation!";
                    this.showInvalidQty = true;
                });
        }
    },

    mounted() {
        let self = this;
        let curr = window.location.href;
        curr = curr.split('/')[4];
        if(curr === "AdminView"){
           self.showDeleted = true;
           self.showActivated = true;
        }
        axios.get('/allmanifestations?isSeller=false')
            .then(response => {
                self.manifestations = response.data;
                for(item of self.manifestations){
                    item.picture = 'data:image/png;base64,' + item.picture;
                    //item.date = new Date(item.date);
                }
            })
            .catch(error => console.log(error));
        axios.get('/manifestationtypes')
            .then(response => {
                self.types = response.data;
                self.types.unshift("Select a type:")
            })
            .catch(error => console.log(error));

        if (this.$route.path.includes("BuyerView"))
            self.showReserveTickets = true;
    }

    }
)