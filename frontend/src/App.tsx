import React, {useState} from 'react';
import { ChangeEvent } from 'react';
import Axios from "axios";
import './App.css';

function App(this: any) {

    Axios.defaults.baseURL = "http://localhost:8080";

    let [raidData, setRaidData] = useState("");
    let [raidType, setRaidType] = useState("");
    let [newDisk, setNewDisk] = useState("");
    let [raidReceived, setRaidReceived] = useState("");
    let [diskSpace, setDiskSpace] = useState("");
    let [diskFreeSpace, setFreeSize] = useState("");
    let [diskUsage, setDiskUsage] = useState("");
    let [diskByIdRead, setDiskByIdRead] = useState("");
    let [diskDelete, setDiskDelete] = useState("");
    let [sectorMulfunction, setSectorMulfunction] = useState("");
    let [voltageSpike, setVoltageSpike] = useState("");
    let [vibrationDamage, setVibrationDamage] = useState("");



    const postNewDisk = () => {
        Axios.post("/disk/register", {
                RegisterDiskRequest:   {
                ipAddress: "dupa",
                port: "test",
                numberOfSectors: 32,
                sizeOfSector: 128,
                isCheckSumDisk: true
            } }).then(
            (response) =>{
                console.log(response);
                setNewDisk(response.data);
            }
        );
    };

    // const postRaidData = () => {
    //     Axios.post("/text/writing", {},{params: {content: raidData}}).then(
    //         (response) =>{
    //             console.log(response);
    //             setRaidData(response.data);
    //         }
    //     );
    // };

    const postRaidType = () => {
        Axios.post("/raid", {},{params: {type: raidType}}).then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const getRaidReceived = () => {
        Axios.get("/raid0/").then(
            (response) => {
                console.log(response);
                var printString = response.data.join(' ');
                setRaidReceived(printString);
            }
        );
    }

    const getDiskSpace = () => {
        Axios.get("raid0/disksize").then(
            (response) => {
                console.log(response);
                setDiskSpace(response.data);
            }
        );
    }

    const getDiskFree = () => {
        Axios.get("raid0/disksizefree").then(
            (response) => {
                console.log(response);
                setFreeSize(response.data);
            }
        );
    }

    const getDiskUsage = () => {
        Axios.get("raid0/diskusage").then(
            (response) => {
                console.log(response);
                setDiskUsage(response.data);
            }
        );
    }


    const getDiskByIdRead = () => {
        Axios.get("/disk/read/{id}").then(
            (response) => {
                console.log(response);
                setDiskByIdRead(response.data);
            }
        );
    }

    const deleteDisk = () => {
        Axios.delete("/disk/{id}").then(
            (response) => {
                console.log(response);
                setDiskDelete(response.data);
            }
        );
    }

    // const checkRaid0 = () => {
    //     raidType = "0";
    //     postRaidType0();
    // }
    //
    // const checkRaid1 = () => {
    //     raidType = "1";
    //     postRaidType1();
    // }
    //
    // const checkRaid2 = () => {
    //     raidType = "2";
    //     postRaidType3();
    // }
    //
    // const onChange = (e: ChangeEvent<HTMLInputElement>)=> {
    //     raidData = e.target.value;
    // }


    const getRaidType0 = () => {
        Axios.get("/raid0/type").then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const getRaidType1 = () => {
        Axios.get("/raid1/type").then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const getRaidType3 = () => {
        Axios.get("/raid3/type").then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };


    const postRaidType0 = () => {
        Axios.post("/raid0/type", {}).then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };


    const postRaidType1 = () => {
        Axios.post("/raid1/type", {}).then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const postRaidType3 = () => {
        Axios.post("/raid3/type", {}).then(
            (response) =>{
                console.log(response);
                setRaidType(response.data);
            }
        );
    };

    const postRaidData = () => {
        Axios.post("/raid0/save", raidData).then(
            (response) =>{
                console.log(response);
                setRaidData(response.data);
            }
        );
    };

    const onChange = (e: ChangeEvent<HTMLInputElement>)=> {
         raidData = e.target.value;
     }

    const postSectorMulfunction= () => {
        Axios.post("/raid0/sectormulfunction", {}).then(
            (response) =>{
                console.log(response);
                setSectorMulfunction(response.data);
            }
        );
    };

    const postVibrationDamage= () => {
        Axios.post("/raid0/vibrationdamage", {}).then(
            (response) =>{
                console.log(response);
                setVibrationDamage(response.data);
            }
        );
    };

    const postVoltageSurge= () => {
        Axios.post("/raid0/voltagesurge", {}).then(
            (response) =>{
                console.log(response);
                setVoltageSpike(response.data);
            }
        );
    };


    // @ts-ignore
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
                                onChange={getRaidType0}
                            />
                            <label htmlFor="f-option">RAID 0</label>

                            <div className="check"></div>

                        </li>

                        <li>
                            <input
                                type="radio"
                                id="s-option"
                                name="selector"
                                onChange={getRaidType1}
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
                                onChange={getRaidType3}
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


            <div className="resultsContainer">
                <div className="raid1">
                    <h1>Wynik</h1>
                    <div className="results">
                        <form>
                            <p>{raidReceived}</p>
                        </form>
                        <a className="button" onClick={getRaidReceived}>Odbierz</a>
                    </div>
                </div>
            </div>


            <div className="statsContainer">
                <div className="statsButtonsContainer">
                    <h2>Statystyki</h2>

                    <ul>
                        <li>
                            Wielkość dysku:
                            {getDiskSpace}
                            <p>{diskSpace}</p>
                        </li>
                        <li>
                            Wolne miejsce:
                            {getDiskFree}
                            <p>{diskFreeSpace}</p>
                        </li>
                        <li>
                            Użycie dysku:
                            {getDiskUsage}
                            <p>{diskUsage}</p>
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

                <h3>Uszkodzone sektory</h3>
                <ul>
                    <li>
                        Identyfikatory uszkodzonych sektorów (awaria):
                    </li>
                </ul>

                <h3>Uszkodzenie wibracyjne</h3>
                <ul>
                    <li>
                        Identyfikatory uszkodzonych sektorów ze względu na wibracje (losowe usunięcie danych):
                    </li>
                </ul>

                <h3>Uszkodzenie przez skok napięcia</h3>
                <ul>
                    <li>
                        Identyfikatory uszkodzonych sektorów ze względu na skok napięcia (tymczasowe):
                    </li>
                </ul>

                </div>
                <div className="radioButtonsContainer">
                    <div className="radioContainer">
                        <div className="radioButtonsContainer">
                            <h3>Wybierz typ uszkodzenia</h3>

                            <ul>
                                <li>
                                    <input
                                        type="radio"
                                        id="f-option"
                                        name="selector"
                                        onChange={postSectorMulfunction}
                                    />
                                    <label htmlFor="f-option">Awaria sektora</label>

                                    <div className="check"></div>

                                </li>

                                <li>
                                    <input
                                        type="radio"
                                        id="s-option"
                                        name="selector"
                                        onChange={postRaidType1}
                                    />
                                    <label htmlFor="s-option">Uszkodzenie wibracyjne</label>

                                    <div className="check">
                                        <div className="inside"></div>
                                    </div>

                                </li>

                                <li>
                                    <input
                                        type="radio"
                                        id="t-option"
                                        name="selector"
                                        onChange={postRaidType3}
                                    />
                                    <label htmlFor="t-option">Skok napięcia</label>

                                    <div className="check">
                                        <div className="inside"></div>
                                    </div>

                                </li>
                            </ul>
                        </div>
                        <form className="dmgForm" action="#" method="post">
                            <h3>Podaj ID sektora do uszkodzenia:</h3>
                            <input type="text" id="f-id" name="selector"></input>
                        </form>
                        {"\n"}
                        <a className="button" onClick={postRaidData}>Wyślij</a>
                    </div>
                </div>
            </div>


        </div>
    );


}
export default App;
