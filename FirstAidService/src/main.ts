import * as express from "express";
import * as bodyParser from "body-parser";
import { DataRequestHandler } from "./handlers/dataRequestHandler"
import { DatabaseAccess } from "./databaseAccess"
import { WeeklyWorkShift } from "./workShiftsVerifier/utils/weeklyWorkShift";
import { WorkShift } from "./workShiftsVerifier/utils/workShift";

let app = express();
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.post("/proxima/medical/firstAid/data", DataRequestHandler.handleDataRequest);

app.listen(9876, () => {
  console.log("HTTP first aid service - listen on port 9876");


  /*var obj = {
    rescuerID: "123",
    monday: [{begin:10, end:50},{begin:85,end:180}],
    tuesday: [{begin:100, end:140},{begin:200,end:300}],
    friday: [{begin:0, end: 800}],
    sunday: [{begin:10, end:50},{begin:85,end:180}]
  }

  console.log(obj); 

  var x: WeeklyWorkShift = new WeeklyWorkShift(obj.rescuerID, obj.monday.map((w) =>  new WorkShift(w.begin, w.end)), obj.tuesday.map((w) =>  new WorkShift(w.begin, w.end)), undefined, undefined,  obj.friday.map(w => new WorkShift(w.begin, w.end)), undefined, obj.sunday.map((w) =>  new WorkShift(w.begin, w.end), obj.friday.map(w => new WorkShift(w.begin, w.end))));

  console.log(x.checkShift(new Date().getDay(), new Date().getMinutes()));
*/
});