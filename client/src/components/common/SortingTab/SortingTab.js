import { useState } from "react"
import { Option, SortWrapper } from "./SortingTabStyle"

const SortingTab = ({ sortMode, setSortMode }) => {

    const sortOption = [{
        mode: "like",
        button: "인기순"
    }, {
        mode: "view",
        button: "조회순"
    }, {
        mode: "recent",
        button: "최신순"
    }]

    const handleClick = (mode) => {
        setSortMode(mode);
    }

    return (
        <SortWrapper>
            {sortOption.map((option, idx) => {
                return (
                    <Option
                        key={idx}
                        onClick={()=> {handleClick(option.mode)}}
                        className={sortMode===option.mode && "selected"}
                        // className="selected"
                    >
                        {option.button}
                    </Option>
                )
            })}
        </SortWrapper>
    )
}

export default SortingTab