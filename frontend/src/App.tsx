import React, {useState} from 'react';
import { ChangeEvent } from 'react';
import Axios from "axios";
import './App.css';

function App(this: any) {

    Axios.defaults.baseURL = "http://localhost:8080";

    let [raidData, setRaidData] = useState("");
    let [raidType, setRaidType] = useState("");
    let [raidReceived, setRaidReceived] = useState("");

    const postRaidData = () => {
        Axios.post("/text/writing", {},{params: {content: raidData}}).then(
            (response) =>{
                console.log(response);
                setRaidData(response.data);
            }
        );
    };

    const postRaidType = () => {
        Axios.post("/raid", {},{params: {type: raidType}}).then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const getRaidReceived = () => {
        Axios.get("/text/reading").then(
            (response) => {
                console.log(response);
                setRaidReceived(response.data);
            }
        );
    }

    const checkRaid0 = () => {
        raidType = "0";
        postRaidType();
    }

    const checkRaid1 = () => {
        raidType = "1";
        postRaidType();
    }

    const checkRaid2 = () => {
        raidType = "2";
        postRaidType();
    }

    const onChange = (e: ChangeEvent<HTMLInputElement>)=> {
        raidData = e.target.value;
    }

    return (
        <div className="App">

            <header className="App-header">
                <h1> Projekt 3 - RAID </h1>
            </header>

            <div className="radioContainer">
                <div className="radioButtonsContainer">
                    <h2>Wybierz RAID</h2>

                    <ul>
                        <li>
                            <input
                                type="radio"
                                id="f-option"
                                name="selector"
                                onChange={checkRaid0}
                            />
                            <label htmlFor="f-option">RAID 0</label>

                            <div className="check"></div>

                        </li>

                        <li>
                            <input
                                type="radio"
                                id="s-option"
                                name="selector"
                                onChange={checkRaid1}
                            />
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
                                onChange={checkRaid2}
                            />
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
                        <input
                            placeholder={"Wpisz tekst"}
                            onChange={onChange}>
                        </input>
                    </form>
                </div>
                <a className="button" onClick={postRaidData}>Wyślij</a>
            </div>


            <div className="Container">
                <div className="resultsContainer">
                    <div className="raid1">
                        <h1>Wynik</h1>
                        <div className="results">
                            <form>
                                <p></p>
                            </form>
                        </div>
                    </div>
                </div>
            </div>


            <div className="statsContainer">
                <div className="statsButtonsContainer">
                    <h2>Statystyki</h2>

                    <ul>
                        <li>
                            Wielkość dysku:
                        </li>
                        <li>
                            Wolne miejsce:
                        </li>
                        <li>
                            Użycie dysku:
                        </li>
                        <li>
                            Użycie dysku w procentach:
                        </li>
                    </ul>

                    <h3>Statystyki użycia sektorów</h3>

                    <ul>
                        <li>
                            Identyfikatory wolnych sektorów:
                        </li>
                        <li>
                            Identyfikatory sektorów, które są używane:
                        </li>
                    </ul>

                    <h3>Trwale uszkodzone sektory</h3>
                    <ul>
                        <li>
                            Identyfikatory sektorów trwale uszkodzonych:
                        </li>
                    </ul>

                </div>
                <div className="radioButtonsContainer">
                    <form className="dmgForm" action="#" method="post">
                        <h3>Podaj ID sektora do uszkodzenia:</h3>
                        <input type="text" id="f-id" name="selector"></input>
                    </form>
                </div>
                <a href="/" className="button">Wyślij</a>
            </div>


        </div>
    );


}
export default App;