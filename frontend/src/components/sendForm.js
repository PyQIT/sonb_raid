import React, {useState} from 'react';
import Axios from "axios";

function SendForm() {

    const[raidData, setRaidData] = useState(null);
    const[raidType, setRaidType] = useState(null);

    const postRaidType = () => {
        Axios.post("/raid").then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const postRaidData = () => {
        Axios.post("/text/writing").then(
            (response) =>{
                console.log(response);
                postRaidData(response.data);
            }
        );
    };


    return (
        <div className="radioContainer">
            <div className="radioButtonsContainer">
                <h2>Wybierz RAID</h2>
                <ul>
                    <li>
                        <input
                            type="radio"
                            id="f-option"
                            name="selector"
                            checked={raidType === '0'}
                            onChange={postRaidType}
                        ></input>
                        <label htmlFor="f-option">RAID 0</label>

                        <div className="check"></div>

                    </li>

                    <li>
                        <input
                            type="radio"
                            id="s-option"
                            name="selector"
                            checked={raidType === '1'}
                            onChange={postRaidType}
                        ></input>
                        <label htmlFor="s-option">RAID 1</label>

                        <div className="check">
                            <div className="inside"></div>
                        </div>

                    </li>

                    <li>
                        <input
                            type="radio"
                            id="t-option"
                            name="selector"
                            checked={raidType === '2'}
                            onChange={postRaidType}
                        ></input>
                        <label htmlFor="t-option">RAID 3</label>

                        <div className="check">
                            <div className="inside"></div>
                        </div>

                    </li>
                </ul>
            </div>
            <div className="radioButtonsContainer">
                <h2>Wpisz tekst do wyslania</h2>
                <form>
                    <textarea placeholder="Wpisz tekst...">{raidData}</textarea>
                </form>
            </div>
            <a className="button" onClick={postRaidData}>Wyślij</a>
        </div>
    );
}

export default SendForm;