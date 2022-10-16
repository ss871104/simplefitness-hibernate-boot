package com.simplefitness.member.repository.impl;

import java.util.List;

import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.simplefitness.member.domain.MemberBean;
import com.simplefitness.member.repository.MemberRepository;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

	@PersistenceContext
	private Session session;

	public Session getSession() {
		return session;
	}
	
	@Override
	public boolean insert(MemberBean member) {
		this.getSession().persist(member);
		return true;
	}

	@Override
	public boolean update(MemberBean member) {
		final StringBuilder hql = new StringBuilder()
				.append("UPDATE MemberBean SET ");
		if (member.getPic() != null) {
			hql.append("pic = :pic, ");
		}
			hql.append("name = :name, ")
				.append("nickname = :nickname, ")
				.append("phone = :phone, ")
				.append("birth = :birth ")
				.append("where username = :username");

			Query<?> query = getSession().createQuery(hql.toString());
			if (member.getPic() != null) {
				query.setParameter("pic", member.getPic());
			}
			return query
					.setParameter("name", member.getName())
					.setParameter("nickname", member.getNickname())
					.setParameter("phone", member.getPhone())
					.setParameter("birth", member.getBirth())
					.setParameter("username", member.getUsername())
					.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Integer memId) {
		if (memId != null) {
			MemberBean bean = this.getSession().get(MemberBean.class, memId);
			if(bean != null && bean.getMemId() != null) {
				this.getSession().delete(bean);
				return true;
			}
		}
		return false;
	}

	@Override
	public MemberBean selectById(Integer memId) {
		if (memId != null) {
			return this.getSession().get(MemberBean.class, memId);
		}
		return null;
	}

	@Override
	public List<MemberBean> selectAll() {
		Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean ", MemberBean.class);
		return query.list();
	}
	
	@Override
	public MemberBean selectByUsername(String username) {
		if (username != null) {
			Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean WHERE username = :username", MemberBean.class);
			query.setParameter("username", username);	
			return query.uniqueResult();
		}
		return null;
	}
	
	@Override
	public MemberBean selectByEmail(String email) {
		if (email != null) {
			Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean WHERE email = :email", MemberBean.class);
			query.setParameter("email", email);	
			return query.uniqueResult();
		}
		return null;
	}

	@Override
	public MemberBean selectByUsernameAndPassword(String username, String password) {
		if (username != null && password != null) {
			Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean WHERE username = :username and password = :password", MemberBean.class);
			query.setParameter("username", username);	
			query.setParameter("password", password);	
			return query.uniqueResult();
		}
		return null;
	}

	@Override
	public MemberBean selectByUsernameAndEmail(String username, String email) {
		if (username != null && email != null) {
			Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean WHERE username = :username and email = :email", MemberBean.class);
			query.setParameter("username", username);	
			query.setParameter("email", email);	
			return query.uniqueResult();
		}
		return null;
	}
	
	@Override
	public MemberBean selectEncodePasswordByVerificationCode(String code) {
		if (code != null) {
			Query<MemberBean> query = this.getSession().createQuery("SELECT new com.simplefitness.member.domain.MemberBean(password) FROM MemberBean WHERE verificationCode = :verificationCode", MemberBean.class);
			query.setParameter("verificationCode", code);	
			return query.uniqueResult();
		}
		return null;
	}

	@Override
	public boolean updatePasswordById(String newPassword, Integer memId) {
		if (memId != null) {
			MemberBean result = this.getSession().get(MemberBean.class, memId);
			if(result != null) {
				result.setPassword(newPassword);
				return true;
			}
		}
		return false;
	}

	@Override
	public MemberBean findByVerificationCode(String code) {
		if (code != null) {
			Query<MemberBean> query = this.getSession().createQuery("FROM MemberBean WHERE verificationCode = :verificationCode", MemberBean.class);
			query.setParameter("verificationCode", code);	
			return query.uniqueResult();
		}
		return null;
	}

	@Override
	public boolean updateVerifiedByEmail(MemberBean member) {
		final StringBuilder hql = new StringBuilder()
				.append("UPDATE MemberBean SET ");
			hql.append("verificationCode = :verificationCode, ")
				.append("verified = :verified ")
				.append("where email = :email");

			Query<?> query = getSession().createQuery(hql.toString());
			return query
					.setParameter("verificationCode", member.getVerificationCode())
					.setParameter("verified", member.isVerified())
					.setParameter("email", member.getEmail())
					.executeUpdate() > 0;
		
	}

	@Override
	public boolean updateVerificationCode(MemberBean member) {
		final StringBuilder hql = new StringBuilder()
				.append("UPDATE MemberBean SET ");
			hql.append("verificationCode = :verificationCode ")
				.append("where email = :email");

			Query<?> query = getSession().createQuery(hql.toString());
			return query
					.setParameter("verificationCode", member.getVerificationCode())
					.setParameter("email", member.getEmail())
					.executeUpdate() > 0;
	}
	
	@Override
	public boolean  updatePasswordByVerificationCode(MemberBean member) {
		final StringBuilder hql = new StringBuilder()
				.append("UPDATE MemberBean SET ");
			hql.append("password = :password, ")
				.append("verificationCode = :null ")
				.append("where verificationCode = :verificationCode");

			Query<?> query = getSession().createQuery(hql.toString());
			return query
					.setParameter("password", member.getPassword())
					.setParameter("null", null)
					.setParameter("verificationCode", member.getVerificationCode())
					.executeUpdate() > 0;
	}

}
