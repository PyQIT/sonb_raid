import React, {useState} from 'react';
import Axios from "axios";
import './App.css';
import SendForm from "./components/sendForm";

function App() {

    Axios.defaults.baseURL = "http://localhost:8080";


    const[raidReceived, setRaidReceived] = useState(null);


    // const getRaidReceived = () => {
    //     Axios.get("/text/reading").then(
    //         (response) => {
    //             console.log(response);
    //             setRaidReceived(response.data);
    //         }
    //     );
    // }
    React.useEffect(() => {
        Axios.get("/text/reading").then(
            (response) => {
            console.log(response);
            setRaidReceived(response.data);
        });
    }, []);




  return (
    <div className="App">

      <header className="App-header">
          <h1> Projekt 3 - RAID </h1>
      </header>

       <SendForm />

        <div className="Container">
        <div className="resultsContainer">
            <div className="raid0">
                <h1>RAID 0</h1>
                <div className="results">
                    <form>
                        <p>Wynik: </p>
                    </form>
                </div>
            </div>

            <div className="raid2">
                <h1>RAID 1</h1>
                <div className="results">
                    <form>
                        <p>{raidReceived}</p>
                    </form>
                </div>
            </div>

            <div className="raid3">
                <h1>RAID 3</h1>
                <div className="results">
                    <form>
                        <p>{raidReceived}</p>
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
