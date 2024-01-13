import React, {useState} from "react";
import "./styles.css";
import SpinnerIcon from "@rsuite/icons/legacy/Spinner";
import CheckCircle from "@mui/icons-material/CheckCircle";

const Confirmation = () => {
    const [load, setLoad] = useState(true);
    setTimeout(() => {
        setLoad(false);
    }, 2000);
    return (
        <>
            {load ? (
                <div className="mainLoad">
                    <SpinnerIcon className="loadIcon" pulse style={{ fontSize: "2em" }} />
                    <h2>Loading...</h2>
                </div>
            ) : (
                <div className="Confirmation">
                    <CheckCircle className="checkIcon" />
                    <h5>
                        Your Transaction has been completed, we will send you a Confirmation email.
                    </h5>
                </div>
            )}
        </>
    );
};

export default Confirmation;
