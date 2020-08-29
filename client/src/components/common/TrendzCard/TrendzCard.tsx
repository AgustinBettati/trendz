import React from 'react';
import './TrendzCard.css';
import logo from "../../../assets/TrendzLogo.png";

type CardProps = {
    title?: string,
    height: number,
    children: React.ReactNode,
}

export const TrendzCard = (props: CardProps) => (
    <div className={"main-container"}>
        <div className={'card'}>
            <div className={'header'}>
                <img className={'trendz-logo'} src={logo} alt={''}/>
                <div className={'divisor'}/>
                <div className={'title'}>{props.title}</div>
                <div className={'body'} style={{"height" : props.height+"px"}}>
                    {props.children}
                </div>
            </div>
        </div>
    </div>

)