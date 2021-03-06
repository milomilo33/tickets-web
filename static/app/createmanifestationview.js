Vue.component('createmanifestationview',{
    data(){
     return{
        manifestation:{
            name:'',
            type:'',
            capacity:0,
            date:'',
            time:'',
            ticketPrice:0.0,
            latitude:0,
            longitude:0,
            address:'',
            picture:'',
        },
         pictureNotUploaded: true,
         mapPosition: {latitude: 45.267136, longitude: 19.833549},
         currentDate: new Date().toISOString().split('T', 1)[0],
     }
    },
    template: `<div>
    <b-row>
        <b-col>
            <b-card
            title="Create a new manifestation"         
            tag="article"            
            class="mb-2"
          >
          <b-form flush>
            <b-form-group label="Name:" label-size="lg" label-class="font-weight-bold" flush>
              <b-form-input id="crma-1" v-model="manifestation.name"/>     
            </b-form-group>
            
            <b-form-group label="Type:" label-size="lg" label-class="font-weight-bold" flush>
              <b-form-input id="crma-2" v-model="manifestation.type"/>     
            </b-form-group>
            
            <b-form-group label="Capacity:" label-size="lg" label-class="font-weight-bold" flush>
              <b-form-input id="crma-3" v-model="manifestation.capacity"/>     
            </b-form-group>
            
            <b-form-group label="Entrance ticket price:" label-size="lg" label-class="font-weight-bold" flush>
              <b-form-input id="crma-4" v-model="manifestation.ticketPrice"/>     
            </b-form-group>
            
            <b-form-group label="Opening date:" label-size="lg" label-class="font-weight-bold" flush>
              <input id="crma5" type="date" v-model="manifestation.date" class="form-control" :min="currentDate"/>
            </b-form-group>
            
            <b-form-group label="Opening time:" label-size="lg" label-class="font-weight-bold" flush>
              <input id="crma-6" type="time" v-model="manifestation.time" class="form-control"/> 
            </b-form-group>
            
            <b-button class="btn-success" style="width: 100%" id="create-crma" v-on:click="createManif()">Create</b-button>
           </b-form>
          </b-card>
        </b-col>
        <b-col>
            <b-container fluid class="p-4" style="background: #7b98bc; height: 100%">
              <b-row>
                <b-col>
                    <b-row>
                      <div id="map" class="map"></div>
                    </b-row>
                    <br/>
                    <b-row>
                    <b-col>
                        <b-form-input id="crma-addr" v-model="manifestation.address"/>
                        <br/>
                        <b-button id="addr-crma" class="btn-danger" v-on:click="searchMap()">Confirm</b-button>
                    </b-col>
                    </b-row>
                </b-col>
                <b-col>
                <label><b>Add a poster via upload:</b></label>
                <input id="inputFileToLoad" type="file" v-on:change="encodeImageFileAsURL()" />
                <b-img :src="manifestation.picture" fluid alt=""></b-img>
                <div id="imgTest"></div>
                
                </b-col>               
              </b-row>
            </b-container>
        </b-col>
    </b-row>
    </div>`,
    methods:{
        encodeImageFileAsURL(){

                let self = this;
                var filesSelected = document.getElementById("inputFileToLoad").files;
                if (filesSelected.length > 0) {
                    var fileToLoad = filesSelected[0];

                    var fileReader = new FileReader();

                    fileReader.onload = function(fileLoadedEvent) {
                        var srcData = fileLoadedEvent.target.result; // <--- data: base64
                        console.log(srcData);
                        self.manifestation.picture = srcData;
                    };
                    fileReader.readAsDataURL(fileToLoad);
                }

        },
        showOnMap(){
            $('#map').empty();
            this.mapPosition.latitude = parseFloat(this.manifestation.latitude);
            this.mapPosition.longitude = parseFloat(this.manifestation.longitude);
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
        searchMap(){
            let self = this;
            $.get(' https://nominatim.openstreetmap.org/search?q=' + self.manifestation.address.replace(' ', '+') + '&format=json&addressdetails=1&limit=1&polygon_svg=1', function (data, status) {
                let fullAddr = data[0].address;
                 fullAddr = (fullAddr.road ? fullAddr.road + ', ' : "")+ (fullAddr.house_number ? fullAddr.house_number + ', ' : "") + (fullAddr.neighbourhood ? fullAddr.neighbourhood + ', ' : '')
                     + (fullAddr.city ? fullAddr.city + ', ' : "") + (fullAddr.country ? fullAddr.country : "");
                self.manifestation.address = fullAddr;
                self.manifestation.latitude = data[0].lat;
                self.manifestation.longitude = data[0].lon;
                console.log(self.manifestation.address);
                self.showOnMap();
            });

        },
        createManif(){
            if(!this.manifestation.name || !this.manifestation.type || !this.manifestation.date || !this.manifestation.time || !this.manifestation.picture || !this.manifestation.address || !this.manifestation.capacity ||!this.manifestation.ticketPrice){
                alert("Please fill in all the fields in the form");
            }else{
                if(!parseInt(this.manifestation.capacity)) {
                    alert("Manifestation capacity entered is not a number!");
                    return;
                }
                if(!parseFloat(this.manifestation.ticketPrice)) {
                    alert("Manifestation ticket price entered is not a number!");
                    return;
                }
                let self = this;
                axios.post('addnewmanifestation', JSON.stringify(this.manifestation))
                    .then(res => {
                        alert(res.data);
                    })
                    .catch(err => {
                        console.log(err);
                    })
            }

        }
    },
    mounted(){
        var map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([this.mapPosition.longitude, this.mapPosition.latitude]),
                zoom: 10
            })
        });
    }
})