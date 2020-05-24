let exec = require('child_process').exec, child;
const express = require('express')
var bodyParser = require('body-parser')
const app = express()
const port = 3000



app.use(bodyParser.json());

app.post('/scores', (req, res) => {
  let payload = JSON.stringify(req.body);
  child = exec('java -jar /home/tharsanan/Projects/PaandiTamilGameHelper/target/PaandiTamilGameMaven-1.0-SNAPSHOT-jar-with-dependencies.jar ' + payload,
  function (error, stdout, stderr){
    let jsonObj = JSON.parse(stdout);
    if(error !== null){
      console.log('exec error: ' + error);
      res.send(error);
    }
    else{
      res.send(jsonObj)
    }
  });

});

app.listen(port, () => console.log(`Example app listening at http://localhost:${port}`));