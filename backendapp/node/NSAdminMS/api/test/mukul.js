const express = require('express');
const router = express.Router();
const mongoose = require('mongoose');
const User = require('../models/user');
const myNum = require('../models/mynum');
const jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer');
const SendOtp = require('sendotp');


const sendOtp = new SendOtp('272480AQypykBbJ8C5cb4514e');

var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'mukulbhatia19966@gmail.com',
        pass: 'nodemailer'
    }
});
var mailOptions = {
    from: 'mukulbhatia19966@gmail.com',
    to: 'mukulbhatia1996@gmail.com',
    subject: 'Sending Email using Node.js',
    text: 'That was easy!'
};


const db = "mongodb://localhost:27017/DB1";
mongoose.Promise = global.Promise;

var cors = require('cors');
router.use(cors({
    origin: 'http://localhost:4200'
}));

mongoose.connect(db, function (err) {
    if (err) {
        console.error("Error " + err);
    }
});

router.get('/', function (req, res) {
    res.send('api works');
});

router.post('/register', function (req, res, next) {
    var user = new User({
        email: req.body.email,
        username: req.body.username,
        contact: req.body.contact,
        password: User.hashPassword(req.body.password),
        creation_dt: Date.now()
    });

    let promise = user.save();

    promise.then(function (doc) {
        return res.status(201).json(doc);
    })

    promise.catch(function (err) {
        return res.status(501).json({ message: 'Error registering user.' })
    })
})

router.post('/login', function (req, res, next) {
    let promise = User.findOne({ username: req.body.username }).exec();

    promise.then(function (doc) {
        if (doc) {
            if (doc.isValid(req.body.password)) {
                console.log(doc.contact);
                // generate token
                let token = jwt.sign({
                    tokenuser: [{
                        username: doc.username,
                        contact: doc.contact
                    }
                    ]
                }, 'secret', { expiresIn: '3h' });

                return res.status(200).json(token);
                transporter.sendMail(mailOptions, function (error, info) {
                    if (error) {
                        console.log(error);
                    } else {
                        console.log('Email sent: ' + info.response);
                    }
                });
            } else {
                return res.status(501).json({ message: ' Invalid Credentials' });
            }
        }
        else {
            return res.status(501).json({ message: 'User email is not registered.' })
        }
    });

    promise.catch(function (err) {
        return res.status(501).json({ message: 'Some internal error' });
    })
})


router.get('/username', verifyToken, function (req, res, next) {
    return res.status(200).json(decodedToken);
});

var decodedToken = '';
function verifyToken(req, res, next) {
    let token = req.query.token;

    jwt.verify(token, 'secret', function (err, tokendata) {
        if (err) {
            return res.status(400).json({ message: ' Unauthorized request' });
        }
        if (tokendata) {
            decodedToken = tokendata;
            next();
        }
    })
}


router.post('/contact', function (req, res, next) {
    let promise = User.findOne({ contact: req.body.contact }).exec();

    promise.then(function (doc) {
        console.log("h")
        if (doc) {
            console.log("p");
            console.log(doc.contact);
            var mynum = new myNum({
                contact: doc.contact,
                otp: Math.floor(1000 + Math.random() * 9000),
                creation_dt: Date.now()
            });
            let promise1 = mynum.save();

            promise1.then(function (doc) {
                console.log(mynum.otp);
                /*
                sendOtp.send(doc.contact, "NeerSeva", mynum.otp, function (error, data) {
                  console.log(data);
                });
               */
                return res.status(201).json(doc.contact);
            });
            promise1.catch(function (err) {
                return res.status(501).json({ message: 'Some internal error' });
            })
        } else {
            console.log("qw");
            console.log(doc);
        }
        promise.catch(function (err) {
            return res.status(501).json({ message: 'Some internal error' });
        })
    })
})

//////////////////////////////////////////////////////////////////
router.post('/verify/:contact', function (req, res, next) {
    console.log(contact = req.params.contact);
    console.log(req.body.otp);
    let promise = User.findOne(
        {
            contact: req.params.contact
        }).exec();
    promise.then(function (doc) {
        if (doc) {
            console.log(doc + "gggggggg");
            let promise1 = myNum.findOne(
                {
                    phno: req.body.phno,
                    otp: req.body.otp
                }).exec();
            promise1.then(function (doc1) {
                if (doc1) {
                    console.log(doc1);
                    //generate token
                    let token = jwt.sign({
                        tokenuser: [{
                            username: doc.username,
                            contact: doc.contact
                        }
                        ]
                    }, 'secret', { expiresIn: '3h' });

                    return res.status(200).json(token);
                } else {
                    return res.status(501).json({ message: 'Invalid OTP.' })
                }
            });
            promise1.catch(function (err) {
                return res.status(501).json({ message: 'Some internal error' });
            })
        } else {
            return res.status(501).json({ message: 'Phone No. is not registered.' })
        }

    });
    promise.catch(function (err) {
        return res.status(501).json({ message: 'Some internal error' });
    })
})



module.exports = router;