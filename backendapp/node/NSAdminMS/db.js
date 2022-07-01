var mysql = require("mysql");
var settings = require("./settings.json");
var connection;
function connectDatabase() {
  if (!connection) {
    connection = mysql.createConnection(settings);
    connection.connect(function (err) {      
      if (!err) {
        console.log("Database is connected!");
      } else {
        console.log("Error connecting database!");
      }
    });
  }
  return connection;
}
module.exports = connectDatabase();
