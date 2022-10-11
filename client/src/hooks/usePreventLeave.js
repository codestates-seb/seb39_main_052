// before unload 인식해서 경고창 띄우기
const usePreventLeave = () => {
    const listener = (event) => {
        event.preventDefault();
        event.returnValue = "";
    };
    const enablePrevent = () => window.addEventListener("beforeunload", listener);
    const disablePrevent = () =>
        window.removeEventListener("beforeunload", listener);
    return { enablePrevent, disablePrevent };
};

export default usePreventLeave;