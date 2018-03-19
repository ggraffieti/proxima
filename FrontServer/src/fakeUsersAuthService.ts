import * as express from "express";

let app = express();

app.get('/authorize', (req, res) => {
    if (req.query.targetID === "TID" && req.query.service === "proxima.medical.firstAid") {
        res.status(200).send("Authorized");
    } else {
        res.status(403).send("Unauthorized");
    }
});

app.listen(8436, function () {
    console.log('Fake users auth service listening on port 8436.');
});