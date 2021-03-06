import React, {useEffect, useState} from 'react';
import {NavLink, withRouter} from 'react-router-dom';
import './AppHeader.css';
import logo from '../../../assets/TrendzLogo.png';
import {MdSearch} from 'react-icons/md';

const AppHeader = ({history, location}: any) => {

    const [toSearch, setToSearch] = useState('')

    useEffect(() => getSearchBarValue(), [location])

    const getSearchBarValue = () => {
        if (location.pathname.includes('search')) {
            setToSearch(location.pathname.split('/')[location.pathname.split('/').length-1])
        } else {
            setToSearch('')
        }
    }

    const handleLogout = () => {
        localStorage.removeItem('token');
        history.push('/');
    }

    const handleSearch = () => {
        toSearch && history.push('/main/search/' + toSearch);
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
                            <input
                                value={toSearch}
                                placeholder={"Search...."}
                                onChange={(e) => setToSearch(e.target.value)}
                                onKeyDown={(e) => {if (e.key === 'Enter') handleSearch()}}
                            />
                            <div className={toSearch ? 'searchbar-button-enabled' : 'searchbar-button-disabled'} onClick={() => handleSearch()}>
                                <MdSearch size={20} color={'white'}/>
                            </div>
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
