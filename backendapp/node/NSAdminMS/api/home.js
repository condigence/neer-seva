var connection = require('../db');
var app = require('../app');

app.get('/', (req, res) => {

  var json ={
    "name" : "NeerSevaAdminMS",
    "cluster_name" : "ns_vstiwary",
    "cluster_uuid" : "Cdw5mOseQsiCyHZtDxmF8Q",
    "version" : {
      "number" : "6.8.0",
      "build_flavor" : "oss",
      "build_type" : "tar",
      "build_hash" : "65b6179",
      "build_date" : "2019-05-15T20:06:13.172855Z",
      "build_snapshot" : false,
      "lucene_version" : "7.7.0",
      "minimum_wire_compatibility_version" : "5.6.0",
      "minimum_index_compatibility_version" : "5.0.0"
    },
    "tagline" : "NeerSeva, Pay for Quality and Service. The Best Water Delevery Solution!"
  };
  
    res.json(json);

});


