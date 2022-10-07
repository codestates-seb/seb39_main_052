import { faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import styled from "styled-components";

const AdminSearchBar = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [searchValue, setSearchValue] = useState("");

  const handleOnkeyPress = (e) => {
    if (e.key === "Enter") {
      setSearchValue(e.target.value);
      setSearchParams({ recipe_id: e.target.value });
    }
  };
  console.log(searchValue);

  return (
    <SearchBar>
      <FontAwesomeIcon icon={faMagnifyingGlass} />
      <SearchInput
        // value={searchValue}
        onKeyPress={handleOnkeyPress}
      ></SearchInput>
    </SearchBar>
  );
};

export default AdminSearchBar;

const SearchBar = styled.div``;
const SearchInput = styled.input``;
