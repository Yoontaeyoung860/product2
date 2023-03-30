package com.kh.product.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO{

  private final NamedParameterJdbcTemplate template;

  /**
   * 등록
   *
   * @param product 상품
   * @return 상품아이디
   */
  @Override
  public Long save(Product product) {
    StringBuffer sb = new StringBuffer();
    sb.append("insert into product(pid,pname,quantity,price) ");
    sb.append("values(product_product_id_seq.nextval, :pname, :quantity, :price) ");

    SqlParameterSource param = new BeanPropertySqlParameterSource(product);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sb.toString(),param,keyHolder,new String[]{"pid"});
    long pid = keyHolder.getKey().longValue();

    return pid;
  }

  /**
   * 조회
   *
   * @param pid 상품아이디
   * @return 상품
   */
  @Override
  public Optional<Product> findById(Long pid) {
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");
    sb.append(" where pid = :id ");
    try {
      Map<String, Long> param = Map.of("id", pid);

      Product product = template.queryForObject(
          sb.toString(), param, new BeanPropertyRowMapper<>(Product.class));
      return Optional.of(product);
    } catch (EmptyResultDataAccessException e) {
      //조회결과가 없는 경우
      return Optional.empty();
    }
  }

  /**
   * 수정
   *
   * @param pid 상품아이디
   * @param product   상품
   * @return 수정된 레코드 수
   */
  @Override
  public int update(Long pid, Product product) {
    StringBuffer sb = new StringBuffer();
    sb.append("update product ");
    sb.append("   set pname = :pname, ");
    sb.append("       quantity = :quantity, ");
    sb.append("       price = :price ");
    sb.append(" where pid = :id ");

    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("pname",product.getPname())
        .addValue("quantity",product.getQuantity())
        .addValue("price",product.getPrice())
        .addValue("id",pid);

    return template.update(sb.toString(),param);
  }

  /**
   * 삭제
   *
   * @param pid 상품아이디
   * @return 삭제된 레코드 수
   */
  @Override
  public int delete(Long pid) {
    String sql = "delete from product where pid = :id ";
    return template.update(sql,Map.of("id",pid));
  }


  /**
   * 전체조회
   *
   * @return 전체조회
   */
  @Override
  public List<Product> findAll() {
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");

    List<Product> list = template.query(
        sb.toString(),
        BeanPropertyRowMapper.newInstance(Product.class)  // 레코드 컬럼과 자바객체 멤버필드가 동일한 이름일경우, camelcase지원
    );

    return list;
  }

  @Override
  public boolean isExist(Long aLong) {
    return false;
  }


}