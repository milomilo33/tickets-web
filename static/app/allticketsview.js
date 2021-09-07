Vue.component('allticketsview', {
        data(){
            return{
                tickets: [],
                dateFrom: '',
                dateTo: '',
                manifestationName: '',
                sortOptions: [
                    {value: 0, text: 'Sort by:'},
                    {value: 1, text: 'Manifestation name (Descending)'},
                    {value: 2, text: 'Manifestation name (Ascending)'},
                    {value: 3, text: 'Ticket price (Descending)'},
                    {value: 4, text: 'Ticket price (Ascending)'},
                    {value: 5, text: 'Date of manifestation (Descending)'},
                    {value: 6, text: 'Date of manifestation (Ascending)'}
                ],
                sortSelected: 0,
                types: [
                    {value: 'Select a ticket type:', text: 'Select a ticket type:'},
                    {value: 'Regular', text: 'Regular'},
                    {value: 'Fan pit', text: 'Fan pit'},
                    {value: 'VIP', text: 'VIP'}
                ],
                typeSelected: 'Select a ticket type:',
                onlyReserved: '',
                priceFrom: 0.0,
                priceTo: 999999.0,
            };
        },

        template: `
        <div>
        <b-form class="px-3" inline>
            <b-form-group label="Search options:" label-size="lg" label-class="font-weight-bold">
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="ticket-search-manifestation-name" placeholder="Manifestation Name" v-model="manifestationName"/>
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="ticket-search-price-from" placeholder="Price (From)" v-model="priceFrom"/>
                <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="ticket-search-price-to" placeholder="Price (To)" v-model="priceTo"/>
    <!--            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="ticket-search-date-from" placeholder="Date (From)" v-model="dateFrom"/>-->
    <!--            <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="ticket-search-date-to" placeholder="Date (To)" v-model="dateTo"/>-->
                <b-form-select v-model="sortSelected" :options="sortOptions" class="mb-2 mr-sm-2 mb-sm-0"></b-form-select>
                <b-button class="mb-2 mr-sm-2 mb-sm-0" v-on:click="search">Search</b-button>
            </b-form-group>
        </b-form>
        <b-form class="px-3" inline>
            <b-form-group label="Date (From): " label-class="font-weight-bold" class="mb-2 mr-sm-2 mb-sm-0">
                <input id="ticket-search-date-from" type="date" placeholder="Date (From)" v-model="dateFrom" class="form-control"/>
            </b-form-group>
            <b-form-group label="Date (To): " label-class="font-weight-bold" class="mb-2 mr-sm-2 mb-sm-0">
                <input id="ticket-search-date-from" type="date" placeholder="Date (From)" v-model="dateTo" class="form-control"/>
            </b-form-group>
            <b-form-group label="Filters: " label-class="font-weight-bold">
                <b-form-select class="mb-2 mr-sm-2 mb-sm-0" :options="types" v-model="typeSelected"></b-form-select>
            </b-form-group>
            <b-form-checkbox v-model="onlyReserved" class="pt-4">Show only reserved tickets</b-form-checkbox>
        </b-form>
        <br/>
        <b-card-group deck>
        <div class="row pl-3">
        <div class="col" v-for="t in tickets" :key="t.id" >
          <b-card
            :title="t.manifestation.name"
            img-src="img/ticket.png"
            img-alt="Image"
            img-top
            tag="article"
            style="max-width: 20rem;"
            class="mb-2"
          >
            <b-list-group flush>
              <b-list-group-item>Ticket type: {{t.type}}</b-list-group-item>
              <b-list-group-item>Date of manifestation: {{dateTimeToDate(t.date)}} at {{dateTimeToTime(t.date)}}</b-list-group-item>
              <b-list-group-item>Ticket price: {{t.price}} RSD</b-list-group-item>
              <b-list-group-item>Status: {{statusConv(t.status)}}</b-list-group-item>
            </b-list-group>
        
<!--            <b-button v-on:click="details(t.id)" variant="primary">Details</b-button>-->
          </b-card>
        </div>        
        </div>
        </b-card-group>
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
            statusConv(status){
                if(status === "REZERVISANA")
                    return "Reserved";
                else
                    return "Cancelled";
            },
            search(e){
                e.preventDefault();
                let searchOptions = '?dateFrom=' + this.dateFrom + '&dateTo=' + this.dateTo + '&sortSelected=' + this.sortSelected +
                    '&onlyReserved=' + this.onlyReserved + '&typeSelected=' + this.typeSelected + '&priceFrom=' +
                    this.priceFrom + '&priceTo=' + this.priceTo + '&manifestationName=' + this.manifestationName;
                let self = this;
                axios.get('ticketsearch' + searchOptions)
                    .then(response => {
                        self.tickets = response.data;
                    })
                    .catch(error => console.log(error));
            }//,
            // details(id){
            //     let curr = window.location.href;
            //     curr = curr.split('/')[4];
            //     this.$router.push({path: curr + '/ManifestationDetails', query: {'id' : id}});
            // }
        },

        mounted() {
            let self = this;
            axios.get('/alltickets')
                .then(response => {
                    self.tickets = response.data;
                })
                .catch(error => console.log(error));
        }

    }
)