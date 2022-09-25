// 무한 스크롤 관련 훅
// 안사용하게 됨, 삭제 예정
import { useState, useEffect, useCallback } from 'react';

// 옵션 값 지정
const defaultOption = {
    root: null, //타겟 요소와 교차 영역을 정의하기 위해 사용하는 상위 요소 프로퍼티 (null 지정 시 viewport가 root)
    threshold: 0.5, //콜백함수를 실행시키기 위한 루트 영역과 타겟 요소와의 교차 영역 비율 (0: 타겟요소가 교차영역 진입했을 때, 0.5: 타겟요소 절반이 교차영역에 들어왔을 때, 1.0: 완전히 교차영역에 진입했을 때)
    rootMargin: '0px' //root 요소에 적용되는 margin 값, 지정한 만큼 교차 영역이 계산되며 루트 범위가 축소하게 된다
};

// 커스텀훅 useIntersect
// 관찰 대상을 지정할 수 있도록 ref값을 useState 훅을 이용해 state로 관리해준다.
// 관찰자를 만들어준다.
const useIntersect = (onIntersect, option) => {
    const [ref, setRef] = useState(null);
    const checkIntersect = useCallback(([entry], observer) => {
        if (entry.isIntersecting) {
            onIntersect(entry, observer);
        }
    }, []); 

    // 관찰자가 언제 관찰하는지, 관찰을 종료하는지에 대해 로직을 구현해준다.
    useEffect(() => {
        let observer;
        if (ref) {
            observer = new IntersectionObserver(checkIntersect, {
                ...defaultOption,
                ...option
            });
            observer.observe(ref);
        }
        return () => observer && observer.disconnect();
    }, [ref, option.root, option.threshold, option.rootMargin, checkIntersect]);

    return [ref, setRef];
};

export default useIntersect;

// Intersection Observe는 오직 무한 스크롤을 구현하기 위한 개념이 아니다.
// 지연 로딩이나, 스켈레톤 UI를 구현할 때도 사용되기 때문에 로직을 분리해 사용한다면 프로잭트 곳곳에서 유용하게 사용할 수 있다.