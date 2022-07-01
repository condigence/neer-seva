var connection = require("../../db");
var app = require("../../app");

///////////////////////////////////////////////////

const SendOtp = require('sendotp');
const jwt = require('jsonwebtoken');

const sendOtp = new SendOtp('272480AQypykBbJ8C5cb4514e');

app.get('/vishapi', function (req, res) {
    res.send('api works');
});

/////////////  Generate Token if user contact is registered AND THEN YOU CAN ACCESS oter apis

app.post('/nsgenToken', function (req, res, next) {
    let contact = req.body.contact;
    console.log(contact);
    let sql = 'SELECT * FROM users WHERE USER_CONTACT = ' + contact;
    let query = connection.query(sql, (err, result) => {
        if (err) throw err;
        if (result.length == 0) {
            //  res.json('users not found');
            return res.status(501).json({ message: 'User contact is not registered.' })
        } else {

            console.log(result[0].USER_CONTACT);
            console.log(result[0].USER_NAME);
            // generate token
            let token = jwt.sign({
                tokenuser: [{
                    username: result[0].USER_NAME,
                    contact: result[0].USER_CONTACT
                }
                ]
            }, 'secret', { expiresIn: '3h' });

            res.status(200).json(token);

        }
    });
});

// app.get('/nsusername', verifyToken, function (req, res, next) {
//     return res.status(200).json(decodedToken);
// })

// var decodedToken = '';
// ////////////////////////////
// function verifyToken(req, res, next) {
//     let token = req.query.token;
//     jwt.verify(token, 'secret', function (err, tokendata) {
//         if (err) {
//             return res.status(400).json({ message: ' Unauthorized request' });
//         }
//         if (tokendata) {
//             decodedToken = tokendata;
//             next();
//         }
//     })
// }

///////////////////////////Tested send and save otp to DB by contact Working FINE
app.post('/nssendotp', function (req, res, next) {
    let contact = req.body.contact;
    console.log(contact);
    let sql = 'SELECT * FROM users WHERE USER_CONTACT = ' + contact;
    let query = connection.query(sql, (err, result) => {
        if (err) throw err;
        if (result.length == 0) {
            //  res.json('users not found');
            return res.status(501).json({ message: 'User contact is not registered.' })
        } else {
            // res.json(result);
            let USER_OTP = Math.floor(1000 + Math.random() * 9000);
            connection.query('UPDATE `users` SET `USER_OTP` = ? where `USER_CONTACT` = ?', [USER_OTP, contact], function (error, results, fields) {
                if (error) throw error;
                console.log(USER_OTP);
                
                // sendOtp.send(contact, "NeerSeva", USER_OTP, function (error, data) {
                //   console.log(data);
                // });
               
                res.send(JSON.stringify(results));
            });
        }
    });
});

////////////////////////////////////////////////////////////////// Tested working

/////////////   call this API when you got OTP  AND YOU WILL BE ABLE TO ACCESS OTHER API USING THIS TOKEN
app.post('/verify/:contact', function (req, res, next) {

    let contact = req.params.contact;
    console.log(req.body.otp);
    console.log(contact);
    let sql = 'SELECT * FROM users WHERE USER_CONTACT = ' + contact +
        ' and USER_OTP = ' + req.body.otp;

    console.log(sql);
    let query = connection.query(sql, (err, result) => {
        if (err) throw err;

        // res.json(result);
        console.log(result[0].USER_CONTACT);
        console.log(result[0].USER_NAME);
        // generate token
        let token = jwt.sign({
            tokenuser: [{
                username: result[0].USER_NAME,
                contact: result[0].USER_CONTACT
            }
            ]
        }, 'secret', { expiresIn: '3h' });

        res.status(200).json(token);
    });
})



module.exports = app;