import React from "react";
import axios from "axios";

const PLAYER_A = "PLAYER_A";
const PLAYER_B = "PLAYER_B";

class Board extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            PLAYER_A_VALUES : [7,7,7,7,7,7,7],
            PLAYER_B_VALUES : [7,7,7,7,7,7,7],
            isAdd : true,
            PROCESSING_TIME : 1,
            STRATEGY:0,
            scores:{},
            scoreList:[],
            indexlist:[],
            scoreIndex:0,
            color:"#ff0000"
        }
        this.buttonClicked = this.buttonClicked.bind(this);
        this.submit = this.submit.bind(this);
        this.prev = this.prev.bind(this);
        this.next = this.next.bind(this);
        this.prevChoice = this.prevChoice.bind(this);
        this.nextChoice = this.nextChoice.bind(this);
    }

    buttonClicked(player, index){
        if(player === PLAYER_A){
            let playerAArr = this.state.PLAYER_A_VALUES;
            if(this.state.isAdd){
                playerAArr[index] = playerAArr[index] + 1;
            }
            else{
                playerAArr[index] = playerAArr[index] - 1;
            }
            this.setState(
                {
                    playerAArr: playerAArr
                }
            );
        }

        if(player === PLAYER_B){
            let playerBArr = this.state.PLAYER_B_VALUES;
            if(this.state.isAdd){
                playerBArr[index] = playerBArr[index] + 1;
            }
            else{
                playerBArr[index] = playerBArr[index] - 1;
            }
            this.setState(
                {
                    playerBArr: playerBArr
                }
            );
        }
    }

    submit(){
        let _this = this;
        let PLAYER_A_VALUES = [];
        let PLAYER_B_VALUES = [];
        let PROCESSING_TIME = 1;
        let STRATEGY = 0;
        for (let index = 0; index < _this.state.PLAYER_A_VALUES.length; index++) {
            if(_this.state.PLAYER_A_VALUES[index] >= 0){
                PLAYER_A_VALUES.push(_this.state.PLAYER_A_VALUES[index]);
            } 
        }
        for (let index = 0; index < _this.state.PLAYER_B_VALUES.length; index++) {
            if(_this.state.PLAYER_B_VALUES[index] >= 0){
                PLAYER_B_VALUES.push(_this.state.PLAYER_B_VALUES[index]);
            } 
        }
        PROCESSING_TIME = this.state.PROCESSING_TIME;
        STRATEGY = this.state.STRATEGY;

        let requestObj = {
            PLAYER_A_VALUES,
            PLAYER_B_VALUES,
            PROCESSING_TIME,
            STRATEGY
        }
        this.setState({
            scoreList:[],
            scores:[],
            scoreIndex:0
        },() => {
            axios.post('/scores', requestObj)
            .then(function (response) {
                  let scoreList = [];
                  let indexlist = [];
                  for (var key in response.data) {
                      if (response.data.hasOwnProperty(key)) {
                          scoreList.push(key);
                          indexlist.push(0);
                      }
                  }
                  console.log(response.data);
                  _this.setState({scores:response.data, scoreList: scoreList, indexlist:indexlist});
            })
            .catch(function (error) {
              console.log(error);
            });
        });
        
    }

    next(){
        if(this.state.scoreIndex === this.state.scoreList.length-1){

            this.setState({
                scoreIndex:0
            });
        }
        else{
            this.setState({
                scoreIndex:this.state.scoreIndex+1
            });
        }
    }

    prev(){
        if(this.state.scoreIndex === 0){
            this.setState({
                scoreIndex:this.state.scoreList.length-1
            });
        }
        else{
            this.setState({
                scoreIndex:this.state.scoreIndex-1
            });
        }
    }

    nextChoice(){

        let indexList = this.state.indexlist;
        if(this.state.scores[this.state.scoreList[this.state.scoreIndex]].length-1 === this.state.indexlist[this.state.scoreIndex]){
            indexList[this.state.scoreIndex] = 0;
            this.setState(
                {
                    indexList:indexList
                }
            );
        }
        else{
            indexList[this.state.scoreIndex] = indexList[this.state.scoreIndex]+1;
            this.setState(
                {
                    indexList:indexList
                }
            );
        }
        this.setState({
            color:getRandomColor()
        });
    }

    prevChoice(){
        let indexList = this.state.indexlist;
        if(0 === this.state.indexlist[this.state.scoreIndex]){
            indexList[this.state.scoreIndex] = this.state.scores[this.state.scoreList[this.state.scoreIndex]].length-1;
            this.setState(
                {
                    indexList:indexList
                }
            );
        }
        else{
            indexList[this.state.scoreIndex] = indexList[this.state.scoreIndex]-1;
            this.setState(
                {
                    indexList:indexList
                }
            );
        }
        this.setState({
            color:getRandomColor()
        });
    }


    render(){
        return(
            <div>
                <div style={{display: 'inline-block'}}>
                    <button style={{ margin: "20px" }} className={this.state.isAdd?"btn btn-success":"btn btn-danger"} onClick={() => {this.setState({
                        isAdd : !this.state.isAdd
                    })}}>{this.state.isAdd?"+":"-"}</button>
                    
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.setState({
                        PROCESSING_TIME : this.state.isAdd? this.state.PROCESSING_TIME + 1:this.state.PROCESSING_TIME - 1
                    })}}>{this.state.PROCESSING_TIME}</button>
                    
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.setState({
                        STRATEGY : this.state.STRATEGY===0?1:0
                    })}}>{this.state.STRATEGY}</button>
                    
                    <button style={{ margin: "20px" }} className="btn btn-success" onClick={() => {
                        this.submit();
                    }}>Submit</button>
                    
                </div>
                <br/>
                <div style={{display: 'inline-block'}}>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,0)}}>{this.state.PLAYER_A_VALUES[0]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,1)}}>{this.state.PLAYER_A_VALUES[1]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,2)}}>{this.state.PLAYER_A_VALUES[2]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,3)}}>{this.state.PLAYER_A_VALUES[3]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,4)}}>{this.state.PLAYER_A_VALUES[4]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,5)}}>{this.state.PLAYER_A_VALUES[5]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_A,6)}}>{this.state.PLAYER_A_VALUES[6]}</button>
                
                </div>
                <div>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,0)}}>{this.state.PLAYER_B_VALUES[0]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,1)}}>{this.state.PLAYER_B_VALUES[1]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,2)}}>{this.state.PLAYER_B_VALUES[2]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,3)}}>{this.state.PLAYER_B_VALUES[3]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,4)}}>{this.state.PLAYER_B_VALUES[4]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,5)}}>{this.state.PLAYER_B_VALUES[5]}</button>
                    <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.buttonClicked(PLAYER_B,6)}}>{this.state.PLAYER_B_VALUES[6]}</button>
                </div>
                {this.state.scoreList.length>0?
                    <div>
                        <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.next()}}>{">"}</button>
                        <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.prev()}}>{"<"}</button>
                    
                        <h4 style={{ margin: "20px" }} > <span className="label label-default">{this.state.scoreList[this.state.scoreIndex]}</span></h4>

                        <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.nextChoice()}}>{">"}</button>
                        <button style={{ margin: "20px" }} className="btn btn-primary" onClick={() => {this.prevChoice()}}>{"<"}</button>
                        <h4 style={{ margin: "20px" }} > <span className="label label-default">{this.state.scores[this.state.scoreList[this.state.scoreIndex]][this.state.indexlist[this.state.scoreIndex]]}</span></h4>
                        <div style={{ margin: "20px", backgroundColor:this.state.color }}>{this.state.scores[this.state.scoreList[this.state.scoreIndex]]}</div>
                    </div>:
                    <div></div>}
            </div>
        );
    }
}

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

export default Board;