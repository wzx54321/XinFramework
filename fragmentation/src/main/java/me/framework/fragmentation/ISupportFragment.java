package me.framework.fragmentation;


/**
 */
interface ISupportFragment extends ISupport {

    /**
     * replace目标Fragment, 主要用于Fragment之间的replace
     *
     * @param toFragment 目标Fragment
     * @param addToBack  是否添加到回退栈
     */
    void replaceFragment(FragmentSupport toFragment, boolean addToBack);

    /**
     * @return 位于栈顶的子Fragment
     */
    FragmentSupport getTopChildFragment();

    /**
     * @return 当前Fragment的前一个Fragment
     */
    FragmentSupport getPreFragment();

    /**
     * @param fragmentClass 目标子Fragment的Class
     * @param <T>           继承自SupportFragment的Fragment
     * @return 目标子Fragment
     */
    <T extends FragmentSupport> T findChildFragment(Class<T> fragmentClass);

    <T extends FragmentSupport> T findChildFragment(String fragmentTag);

    /**
     * 子栈内 出栈
     */
    void popChild();

    /**
     * 子栈内 出栈到目标Fragment
     *
     * @param targetFragmentClass          目标Fragment的Class
     * @param includeTargetFragment   是否包含目标Fragment
     */
    void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment);

    void popToChild(String targetFragmentTag, boolean includeTargetFragment);

    /**
     * 子栈内 出栈到目标Fragment,并在出栈后立即进行Fragment事务(可以防止出栈后,直接进行Fragment事务的异常)
     *
     * @param targetFragmentClass               目标Fragment的Class
     * @param includeTargetFragment        是否包含目标Fragment
     * @param afterPopTransactionRunnable  出栈后紧接着的Fragment事务
     */
    void popToChild(Class<?> targetFragmentClass, boolean includeTargetFragment, Runnable afterPopTransactionRunnable);

    void popToChild(String targetFragmentTag, boolean includeTargetFragment, Runnable afterPopTransactionRunnable);
}
