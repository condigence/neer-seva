var connection = require("../db");
var app = require("../app");

/////////////////////////////////////////////////////////////////
// login API
app.post("/neerseva/api/v1/login", (req, res) => {
  var postData = req.body;
  let contact = req.body.contact;
  let password = req.body.password;
  let sql =
    "SELECT * FROM user WHERE contact = '" +
    contact +
    "' and password = '" +
    password +
    "' ";
  connection.query(sql, postData, (err, result) => {
    if (err) throw err;
    //if (result.length > 0) {
    if (result) {
      res.json(result);
    } else {
      res.json(err);
    }
    //}
  });
});

////////////////////////

app.post("/neerseva/api/v1/users/authenticate", (req, res) => {
  var postData = req.body;

  let contact = req.body.contact;
  console.log(contact);
  if (!contact || contact == '') {
    console.log(" No Contact provided");
    return res
      .status(400)
      .send({ error: true, message: "Please provide contact" });
  }

  let sql = "SELECT * FROM user WHERE contact = "+JSON.stringify(req.body.contact);

  console.log(sql);
  connection.query(sql, postData, function(error, results, fields) {
    if (error) throw error;
    if (results.length > 0) {
      console.log(results[0]);

      return res.send({
        error: false,
        data: results[0],
        message: "user Found."
      });
    } else {
      return res.status(404).send("Sorry, This Contact is not Registered!");
    }
  });
});
///////////////////////////neerseva/api/v1/
app.post("/neerseva/api/v1/users/otplogin", (req, res) => {
  var postData = req.body;
  let sql =
    "SELECT * FROM user WHERE contact = " +
    req.body.contact +
    " AND otp = " +
    req.body.otp;
  connection.query(sql, postData, function(error, results, fields) {
    if (error) throw error;
    if (results.length > 0) {
      return res.send({ error: false, data: results[0], message: "VALID OTP" });
    } else {
      return res.status(404).send("Sorry, Invalid OTP. Try again!");
    }
  });
});
