Vue.component('manifestationdetailsview', {
    data(){
        return{
            manifestation: null,
            remainingTickets: 0,
            comments: {},
            pos: {latitude: 45.2716, longitude: 19.8478},
            noComments: false
        }
    },
    template: `
    <div>
    <b-row>
        <b-col>
            <b-card
            :title="manifestation.name"         
            tag="article"            
            class="mb-2"
          >
            <b-list-group flush>
              <b-list-group-item>Type: {{manifestation.type}}</b-list-group-item>
              <b-list-group-item>Total Capacity: {{manifestation.capacity}}</b-list-group-item>
              <b-list-group-item>Remaining tickets: {{remainingTickets}}</b-list-group-item>
              <b-list-group-item>Status: {{activeConv(manifestation.active)}}</b-list-group-item>
              <b-list-group-item>Opens on: {{dateTimeToDate(manifestation.date)}} at {{dateTimeToTime(manifestation.date)}}</b-list-group-item>
              <b-list-group-item>Address: {{manifestation.location.address}} ({{manifestation.location.latitude}}, {{manifestation.location.longitude}})</b-list-group-item>
              <b-list-group-item>Entrance ticket: {{manifestation.ticketPrice}} RSD</b-list-group-item>
              <b-list-group-item>Rating: {{ratingConv(manifestation.rating)}}</b-list-group-item>
            </b-list-group>
          </b-card>
        </b-col>
        <b-col>
            <b-container fluid class="p-4 bg-dark">
              <b-row>
                <b-col>
                      
                </b-col>
                <b-col>
                  <b-img  fluid :src="manifestation.picture" alt="Image"></b-img>
                </b-col>               
              </b-row>
            </b-container>
        </b-col>
    </b-row>
    <b-row>
    <label class="font-weight-bold col-1 pt-3" style="font-size:1vw">Comments:</label>   
     <div class="pl-3 pr-3" style="width: 100%;">    
     <h5 v-if="noComments">No comments yet</h5>
    <div class="col" v-for="c in comments" :key="c.id" >
          <b-card
            tag="article"
            title-class="col-1"
          >
            <b-card-text class="col-1 font-weight-bold" style="font-size: large">
                {{c.user.username}}
            </b-card-text>
            <b-card-text class="col-1">
                {{c.text}}
            </b-card-text>
            <b-card-text class="col-1">
                Rated: {{ratingConv(c.rating)}}
            </b-card-text>
          </b-card>
        </div>
     </div>
    </b-row>
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
        activeConv(status){
            return status ? "Active" : "Inactive";
        },
        centerMap(){
            this.pos.latitude = parseFloat(this.manifestation.location.latitude);
            this.pos.longitude = parseFloat(this.manifestation.location.longitude);
            this.manifestation.latitude = this.pos.latitude;
            this.manifestation.longitude = this.pos.longitude;
            console.log(this.manifestation.latitude);
            let self=this;
            let map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: ol.proj.fromLonLat([self.pos.longitude, self.pos.latitude]),
                    zoom: 17
                })
            });
        },
    },
    mounted(){
        let self = this;
        axios.get('manifestationdetails/' + this.$route.params.id)
            .then(res =>{
                self.manifestation = res.data;
                self.manifestation.picture = 'data:image/png;base64,' + self.manifestation.picture;
                self.centerMap();
            })
            .catch(err => {
                console.error(err);
            });
        axios.get('remainingtickets/' + this.$route.params.id)
            .then(res =>{
                self.remainingTickets = res.data;
            })
            .catch(err => {
                console.error(err);
            });
        axios.get('manifestationcomments/' + this.$route.params.id)
            .then(res =>{
                self.comments = res.data;
                if(self.comments.length === 0)
                    self.noComments = true;
            })
            .catch(err => {
                console.error(err);
            });
    }
    }
)