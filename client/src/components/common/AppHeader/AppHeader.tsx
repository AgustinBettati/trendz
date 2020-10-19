import React from 'react';
import {NavLink, withRouter} from 'react-router-dom';
import './AppHeader.css';
import logo from '../../../assets/TrendzLogo.png';
import {MdSearch} from 'react-icons/md';

const AppHeader = ({history}: any) => {

    const handleLogout = () => {
        localStorage.removeItem('token');
        history.push('/');
    }

    return (
        <div style={{}}>
            <header className={"app-header"}>
                <div className={"app-options"}>
                    <div className={"app-branding"}>
                        <NavLink to={"/main/home"}>

                            <img src={logo} alt={''} style={{height: '100%'}}/>

                        </NavLink>
                    </div>
                    <div className={"searchbar-container"}>
                        <div className={"searchbar"}>
                            <MdSearch size={20} color={'white'}/>
                            <input placeholder={"Search...."} style={{width: '500px', height: "30%",}}></input>
                        </div>
                    </div>
                    <NavLink to={"/main/profile"} className={"profile"}>Profile</NavLink>
                    <div className={'logout'} onClick={() => handleLogout()}>Logout</div>
                </div>
            </header>
        </div>
    )
}

export default withRouter(AppHeader);
