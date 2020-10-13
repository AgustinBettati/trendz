import React from 'react';
import { NavLink, withRouter} from 'react-router-dom';
import './AppHeader.css';
import logo from '../../../assets/TrendzLogo.png';

const AppHeader = ({history}: any) => {

    const handleLogout = () => {
        localStorage.removeItem('token');
        history.push('/');
    }

    return (
        <header className={"app-header"}>
            <NavLink to={"/main/home"} >
            <div className={"app-branding"}>
                    <img  src={logo} alt={''}/>
            </div>
            </NavLink>
            <div className={"app-options"}>
                <div style={{display: 'flex', flexDirection: 'row', height: '100%'}}>
                    <div className={"app-option"}>
                    <input placeholder={"Search...."} style={{width: "100%", height:"30%"}} ></input>
                        </div>
                </div>
            </div>
            <NavLink to={"/main/profile"} className={"profile"}>Profile</NavLink>
            <div className={'logout'} onClick={() => handleLogout()}>Logout</div>
        </header>
    )
}

export default withRouter(AppHeader);
