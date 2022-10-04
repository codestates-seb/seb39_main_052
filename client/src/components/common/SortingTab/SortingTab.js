import { useState } from "react"
import { Option, SortWrapper } from "./SortingTabStyle"

const SortingTab = ({ sortMode, setSortMode, setIsRefreshNeeded }) => {

    const sortOption = [{
        mode: "HEART",
        button: "인기순"
    }, {
        mode: "VIEW",
        button: "조회순"
    }, {
        mode: "RECENT",
        button: "최신순"
    }]

    const handleClick = (mode) => {
        setSortMode(mode);
        setIsRefreshNeeded(true); // 데이터 받아오는 서버 요청에서 재로드 할 수 있도록 상태 변경
    }

    return (
        <SortWrapper>
            {sortOption.map((option, idx) => {
                return (
                    <Option
                        key={idx}
                        onClick={() => { handleClick(option.mode) }}
                        className={sortMode === option.mode && "selected"}
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