import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import styled from "styled-components";

const AdminSearchBar = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [searchValue, setSearchValue] = useState("");

  const handleInput = (e) => {
    setSearchValue(e.target.value);
    //레시피 관리일때
    setSearchParams({ recipe_id: e.target.value });
  };
  console.log(searchValue);

  return (
    <SearchBar>
      <FontAwesomeIcon icon={faMagnifyingGlass} />
      <SearchInput value={searchValue} onChange={handleInput}></SearchInput>
    </SearchBar>
  );
};

export default AdminSearchBar;

const SearchBar = styled.div``;
const SearchInput = styled.input``;
