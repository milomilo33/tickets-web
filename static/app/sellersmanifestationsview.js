Vue.component('sellersmanifestationsview', {

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
                priceFrom: '',
                priceTo: '',
                name: '',
            };
        },

        template: `
        <div>
        <h1><b>Manifestations</b></h1>
        <br/>
        <b-form class="px-3" inline>
        <b-form-group label-size="lg" label-class="font-weight-bold">
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
            style="width: 330px; height: 650px"
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
            <b-button v-on:click="deleteManif(m.id)" variant="success">Edit</b-button>
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
                    this.priceFrom + '&priceTo=' + this.priceTo + '&name=' + this.name + '&isSeller=true';
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
                this.$router.push({path:'ManifestationDetails', query: {'id' : id}});
            },
            deleteManif(id){
                this.$router.push({path:'UpdateManifestation', query: {'id' : id}});
            },
        },

        mounted() {
            let self = this;
            axios.get('/allmanifestations?isSeller=true')
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
        }

    }
)