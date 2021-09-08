Vue.component('manifestationdetailsview', {
    data(){
        return{
            manifestation: {
                date: {
                    date:{
                        day:0,
                        month:1,
                        year:0
                    },
                    time:{
                        hour: 0,
                        minute: 0
                    }
                },
                location:{

                }
            },
            remainingTickets: 0,
            comments: {},
            mapPosition: {latitude: 45.267136, longitude: 19.833549},
            noComments: false,
            comment: {
                rating: 0,
                text: '',
                manifestationId: '',
            },
            canComment: false,
            ratingOptions: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
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
                     <div id="map" class="map"></div> 
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
    <b-button id="leaveComment" v-if="canComment" v-b-modal.modal-comment>Leave a comment</b-button>   
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
            <b-button variant="success" v-if="showComment(c)" v-on:click="approveComment(c.id)">Approve</b-button>
            <b-button variant="danger" v-if="showDelete()" v-on:click="deleteComment(c.id)">Delete</b-button>
          </b-card>
        </div>
     </div>
    </b-row>
        
    <b-modal id="modal-comment" title="Write your comment" hide-footer centered>
    <b-row>
    <b-col><label class="font-weight-bold">Rate:</label></b-col>
     <b-col>
    <b-form-select v-model="comment.rating" :options="ratingOptions" class="mb-2 mr-sm-2 mb-sm-0"></b-form-select>
    </b-col>
    </b-row>
    <br/>
    <b-form-textarea
      id="textarea-comment"
      v-model="comment.text"
      placeholder="Enter your comment..."
      rows="3"
      max-rows="6"
    ></b-form-textarea>
     <b-button class="mt-3" variant="primary" style="width: 100%" v-on:click="addComment">Submit</b-button>
    <b-button class="mt-3" block @click="$bvModal.hide('modal-comment')">Cancel</b-button>
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
        activeConv(status){
            return status ? "Active" : "Inactive";
        },
        showOnMap(){
            this.mapPosition.latitude = parseFloat(this.manifestation.location.latitude);
            this.mapPosition.longitude = parseFloat(this.manifestation.location.longitude);
            this.manifestation.latitude = this.mapPosition.latitude;
            this.manifestation.longitude = this.mapPosition.longitude;
            let self=this;
            var map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    })
                ],
                view: new ol.View({
                    center: ol.proj.fromLonLat([self.mapPosition.longitude, self.mapPosition.latitude]),
                    zoom: 17
                })
            });
        },
        addComment(e){
            e.preventDefault();
            if(!this.comment.text){
                alert("Please enter some text for your comment.");
                return;
            }
            axios.post('createcomment', JSON.stringify(this.comment))
                .then(res => {alert('Comment submitted!\n Awaiting moderator approval.')})
                .catch(err => console.error(err));
        },
        showComment(c){
            let curr = window.location.href;
            curr = curr.split('/')[4];
            return !c.approved && curr === 'SellerView';
        },
        showDelete(){
            let curr = window.location.href;
            curr = curr.split('/')[4];
            return curr === 'AdminView';
        },
        refreshManifestation(){
            let self = this;
            axios.get('manifestationcomments/' + self.comment.manifestationId)
                .then(res =>{
                    self.comments = res.data;
                    if(self.comments.length === 0)
                        self.noComments = true;
                })
                .catch(err => {
                    console.error(err);
                });
            axios.get('manifestationdetails/' + self.comment.manifestationId)
                .then(res =>{
                    self.manifestation.rating = res.data.rating;
                })
                .catch(err => {
                    console.error(err);
                });
        },
        approveComment(id){
            let self = this;
            axios.post('approvecomment/' + id)
                .then(res => {
                    alert('Comment approved!');
                    self.refreshManifestation();
                })
        },
        deleteComment(id){
            let self = this;
            axios.post('deletecomment/' + id)
                .then(res => {
                    alert('Comment deleted!');
                    self.refreshManifestation();
                })
        }
    },
    mounted(){
        let self = this;
        self.comment.manifestationId = this.$route.query.id;
            axios.get('manifestationdetails/' + this.$route.query.id)
            .then(res =>{
                self.manifestation = res.data;
                self.manifestation.picture = 'data:image/png;base64,' + self.manifestation.picture;
                self.showOnMap();
            })
            .catch(err => {
                console.error(err);
            });
        axios.get('remainingtickets/' + this.$route.query.id)
            .then(res =>{
                self.remainingTickets = res.data;
            })
            .catch(err => {
                console.error(err);
            });
        axios.get('manifestationcomments/' + this.$route.query.id)
            .then(res =>{
                self.comments = res.data;
                if(self.comments.length === 0)
                    self.noComments = true;
            })
            .catch(err => {
                console.error(err);
            });
        axios.get('cancomment/' + this.$route.query.id)
            .then(res => {
                self.canComment = (res.data === 'true');
            })
            .catch(err => console.error(err))
    }
    }
)