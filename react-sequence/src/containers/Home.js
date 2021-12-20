import * as React from 'react';
import LogoMenu from "./LogoMenu";
import FileView from "./FileView";
import SimulationView from "./SimulationView";

const Home = () => {

    const [isFileView, setIsFileView] = React.useState(true);
    const [isSimulationView, setIsSimulationView] = React.useState(false);

    const activateFileView = () => {
        setIsFileView(true);
        setIsSimulationView(false);
    };

    const activateSimulationView = () => {
        setIsFileView(false);
        setIsSimulationView(true);
    };


    return (
        <div>
            <LogoMenu fileViewCallback={activateFileView}
                      simulationViewCallback={activateSimulationView}/>
            {isFileView ? <FileView/> : null}
            {isSimulationView ? <SimulationView/> : null}

        </div>
    );
}

export default Home;