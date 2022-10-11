import { useState, useEffect, useMemo } from "react";
import { Wrapper, Pages, Button } from "./PaginationStyle";

const Pagination = ({ page, setPage, totalPages }) => {

    // 전체 페이지 수만큼 값이 담긴 배열 (ex: [1,2,3,4,5,...])
    const totalArr = useMemo(() => {
        return  Array.from({length: totalPages}, (_, i) => i + 1);
    }, [totalPages])

    const [showingArr, setShowingArr] = useState(totalArr.slice(0, 10)); // 보여지는 버튼 배열
    const [isStartEllipsisOn, setIsStartEllipsisOn] = useState(false); // 버튼 앞 ...
    const [isEndEllipsisOn, setIsEndEllipsisOn] = useState(false); // 버튼 뒤 ...
    const [start, setStart] = useState(0); //보이는 버튼 중 가장 왼쪽에 오는 수 - 1 (totalArr내 index)
    const [end, setEnd] = useState(10) //보이는 버튼 중 가장 오른쪽에 오는 수 - 1 (totalArr내 index)


    useEffect(() => {
        if (page > totalPages) {
            setPage(1);
        }
        // 페이지네이션의 첫 버튼이 1이 아닌 경우 앞에 ... 표시
        if (start !== 0) {
            setIsStartEllipsisOn(true);
        }
        // 페이지네이션의 첫 버튼이 1인경우 앞에 ... 표시 제거
        if (start === 0) {
            setIsStartEllipsisOn(false);
        }
        // 페이지네이션의 끝 버튼이 가장 마지막 페이지가 아닌 경우 뒤에 ... 표시 
        if (end < totalPages) {
            setIsEndEllipsisOn(true);
        }
        // 페이지네이션의 끝 버튼이 가장 마지막 페이지인 경우 경우 뒤에 ... 표시 제거
        if (end >= totalPages) {
            setIsEndEllipsisOn(false);
        }
        // 현재 페이지가 10 이하인 경우
        if (page <= 10) {
            setStart(0);
            setEnd(start+10);
            setShowingArr(totalArr.slice(start, end));
            setIsStartEllipsisOn(false);
        }
        // 현재 페이지가 10 초과인 경우
        // 페이지네이션의 첫 버튼은 1, 11, 21, 31
        if (page > 10) {
            setStart(Math.floor((page-1)/10) * 10);
            setEnd(start+10);
            setShowingArr(totalArr.slice(start, end));
        }
    }, [page, totalArr, totalPages, start, end])

    
    return (
        <>
            <Wrapper>
                <Pages>
                    <Button
                        onClick={() => setPage(page - 1)}
                        disabled={page === 1} //cannot click when on first page
                        >
                        &lt;
                    </Button>
                    {isStartEllipsisOn && <Button onClick={() => setPage(1)}>...</Button>}
                    {showingArr.map((el, i) => (
                            <Button
                                key={i}
                                onClick={() => setPage(el)}
                                aria-current={page === el ? "page" : null}
                            >
                                {el}
                            </Button>
                        ))}
                    {isEndEllipsisOn && <Button onClick={() => setPage(totalPages)}>...</Button>}
                    <Button
                        onClick={() => setPage(page + 1)}
                        disabled={page === totalPages} // 마지막 페이지에서는 클릭할 수 없음
                    >
                        &gt;
                    </Button>
                </Pages>
            </Wrapper>


        </>
    );
};

export default Pagination;